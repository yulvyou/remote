package com.example.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.remote.constants.RConstant;
import com.example.remote.factory.ThreadPoolFactory;
import com.example.remote.interf.ExecuteCommand;
import com.example.remote.interfimpl.ExecuteUpdateCommand;
import com.example.remote.interfimpl.TestCommand;
import com.example.remote.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 客户端
 */
@Slf4j
@Component
@Configuration
@EnableScheduling
@EnableAsync
public class Clint {

    int version = 1;
    @Scheduled(fixedDelay = 15000)
    @Async
    public void run() {
        try {
            int v = version;
            log.info("定时任务run方法");
            //是否为第一次启动
            if (isFirstLunch()) {
                //设置为开机自启动
                setAutoLunch();
                //修改配置文件（isFirstLaunch改为false）
                fixconfigFile();
            }

            //获取相关信息
            String schoolId = FileUtil.getKeyFromJsonFile(RConstant.TEST_CURRENT_CONFIGFILE_PATH,"schoolID");
            String clientNo = FileUtil.getKeyFromJsonFile(RConstant.TEST_CURRENT_CONFIGFILE_PATH,"clientNo");
//        String version = FileUtil.getKeyFromJsonFile(RConstant.TEST_CURRENT_CONFIGFILE_PATH,"version");
            String plugins = FileUtil.getKeyFromJsonFile(RConstant.TEST_CURRENT_CONFIGFILE_PATH,"plugins");
            String macAddr = IpConfigUtils.getMACAddress();


            //TODO 向服务端请求本客户端需要执行的命令(要改参数)
            String command = null;
            try {
                command = requestCommandsFromServer(schoolId,clientNo, String.valueOf(version),macAddr,plugins);
                if(command == null || command.equals(""))
                    return;
            }catch (Exception e){
                log.error("访问服务器失败,原因：{}",e);
                return;
            }
            //将从服务器得到的命令（String类型）转为Json格式
            JSONObject commandJson = JSONObject.parseObject(command);

            //如果没有需要执行的命令直接返回
            if(commandJson.get("haveCommands") == null){
                return;
            }


            //TODO
            version ++;
            log.info("Command{}: {}",version,command);

            //执行命令
            ExecutorService executor = ThreadPoolFactory.getNormalPool();
            String finalCommand = command;
            Future<Boolean> future = executor.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return executeCommand(commandJson);
                }
            });

            //监听任务（监听此次执行命令操作，如果超过duration分钟命令还没有执行完成，那么就取消这次任务）
            ThreadPoolUtils.monitorTask(future,Integer.valueOf(commandJson.getString("duration")));

            //TODO
            int j = ((ThreadPoolExecutor)executor).getActiveCount();
            log.info("第"+v+"次任务完成："+j + "");
        }catch (Exception e){
            log.error("执行任务失败,原因：{}",e.getMessage());
        }

    }//run


    /**
     * 判断是否为第一次启动
     * @return
     */
    public Boolean isFirstLunch() {
        try {
            //从本程序中的配置文件中查看是不是第一次启动
            String isFirstLaunch = FileUtil.getKeyFromJsonFile(RConstant.TEST_UPDATE_CONFIGFILE_PATH,"isFirstLaunch");
            if (isFirstLaunch.equals("true")){
                return true;
            }else
                return false;
        } catch (Exception e) {
            log.error("读取R程序的配置文件失败,原因：{}",e);
            return false;
        }
    }


    /**
     * 将本程序设置为开机自启动
     */
    public void setAutoLunch() {

    }



    /**
     * 修改配置文件
     */
    public void fixconfigFile() {
        try {
            //读取本程序的配置文件
            String configJson = FileUtil.readJsonFromFile(RConstant.TEST_UPDATE_CONFIGFILE_PATH);
            JSONObject config = new JSONObject();
            config = JSON.parseObject(configJson);
            //将配置文件中的"isFirstLaunch"属性改为false，表示已经不是第一次启动了
            config.replace("isFirstLaunch",false);
            //写入配置文件
            FileUtil.writeJsonToFile(RConstant.TEST_UPDATE_CONFIGFILE_PATH,config.toString());
        }catch (Exception e){
            log.error("修改配置文件失败,原因：{}",e);
            return;
        }

    }


    /**
     * 向服务器请求本客户端需要执行的指令
     * @param schoolId 学校id
     */
    public String requestCommandsFromServer(String schoolId, String clientNo,String version,String macAddr,String plugins) {

        //TODO 模拟从服务器获取数据
        String result = WebUtils.getJsonStrFromGetUrl(RConstant.TEST_SERVER_URL+"/command/get?version="+version);

        return result;
    }

    /**
     * 执行命令
     * @param commandJson
     */
    public boolean executeCommand(JSONObject commandJson) {
//        JSONObject commandJson = JSONObject.parseObject(Command);


        try {
            ExecuteCommand executeCommand = null;
            //查询成功，且有命令需要执行
            if (commandJson.get("resultCode").equals("0") && commandJson.getString("commandID") != null) {
                //下载jar包
                boolean jarDownloaded = downloadJar(commandJson);

                //实例化ExecuteCommand对象
                if(jarDownloaded){
                    //TODO  需要释放注释
//                    executeCommand = (ExecuteCommand) FileUtil.loadObjectFromJar(commandJson.getString("jarPath"),commandJson.getString("classPath"));
                    executeCommand = new TestCommand();
                }else{
                    log.info("下载jar包失败");
                    return false;
                }

                //执行相关操作
                if(executeCommand!=null){
                    //下载文件
                    try {
                        executeCommand.downloadFile(commandJson);
                    }catch (Exception e){
                        log.error("下载文件失败,原因：{}",e);
                        return false;
                    }

                    //关闭程序
                    try {
                        executeCommand.closeApp(commandJson);
                    }catch (Exception e){
                        log.error("关闭程序失败,原因：{}",e);
                        return false;
                    }


                    //安装更新包
                    try {
                        executeCommand.installApp(commandJson);
                    }catch (Exception e){
                        log.error("安装更新包失败,原因：{}",e);
                        return false;
                    }

                    //打开程序
                    try {
                        executeCommand.openApp(commandJson);
                    }catch (Exception e){
                        log.error("打开程序失败,原因：{}",e);
                        return false;
                    }

                    //执行命令
                    try {
                        executeCommand.execute(commandJson);
                    }catch (Exception e){
                        log.error("执行命令失败,原因：{}",e);
                        return false;
                    }

                    //通知服务器
                    try {
                        executeCommand.noticeServer(commandJson);
                        return true;
                    }catch (Exception e){
                        log.error("通知服务器失败,原因：{}",e);
                        return false;
                    }

                }else {
                    log.info("ExecuteCommand对象为空");
                    return false;
                }

            }else{
                log.info("没有需要执行的命令");
                return false;
            }

        }catch (Exception e){
            log.error("执行命令发生异常,原因：{}",e);
            return false;
        }

    }

    /**
     * 下载jar包
     */
    private boolean downloadJar(JSONObject commandJson) {
        try {
            //获取下载jar包需要的参数
            String jarUrl = commandJson.getString("jarUrl");
            String jarPath = commandJson.getString("jarPath");
            String hashCode = commandJson.getString("jarHashCode");
            if (!CommonUtils.isNullOrBlank(jarUrl,jarPath,hashCode)){ //如果参数都不为空则下载jar包
                return FileUtil.downloadFileAndCheck(jarUrl,jarPath,hashCode);
            }else {
                return false;
            }
        }catch (Exception e){
            log.error(",原因：{}",e);
            return false;
        }



    }


}//END
