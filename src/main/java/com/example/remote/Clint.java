package com.example.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.remote.constants.RConstant;
import com.example.remote.factory.CommandFactory;
import com.example.remote.interf.ExecuteCommand;
import com.example.remote.utils.FileUtil;
import com.example.remote.utils.IpConfigUtils;
import com.example.remote.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 客户端
 */
@Slf4j
@Configuration
@EnableScheduling
public class Clint {

    int version = 1;
    @Scheduled(fixedRate = 5000)
    public void run() {

        log.info("定时任务run方法");
        //是否为第一次启动
        if (isFirstLunch()) {
            //设置为开机自启动
            setAutoLunch();
            //修改配置文件（isFirstLaunch改为false）
            fixconfigFile();
        }

        String schoolId = FileUtil.getKeyFromJsonFile(RConstant.TEST_CONFIGFILE_PATH,"schoolID");
        String clientNo = FileUtil.getKeyFromJsonFile(RConstant.TEST_CONFIGFILE_PATH,"clientNo");
//        String version = FileUtil.getKeyFromJsonFile(RConstant.TEST_CONFIGFILE_PATH,"version");
        String plugins = FileUtil.getKeyFromJsonFile(RConstant.TEST_CONFIGFILE_PATH,"plugins");
        String macAddr = IpConfigUtils.getMACAddress();
        //TODO 向服务端请求本客户端需要执行的命令(要改参数)
        String Command = requestCommandsFromServer(schoolId,clientNo, String.valueOf(version),macAddr,plugins);
        log.info("Command{}: {}",version,Command);

        //TODO
        version ++;

        executeCommand(Command);

//        //执行相关命令
//        Future<?> future = ThreadPoolFactory.getNormalPool().submit(new Runnable() {
//            @Override
//            public void run() {
//                executeCommand(Command);
//            }
//        });
//
//        try {
//            future.get(5,TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }


    }//run


    /**
     * 判断是否为第一次启动
     * @return
     */
    public Boolean isFirstLunch() {
        try {

            String isFirstLaunch = FileUtil.getKeyFromJsonFile(RConstant.TEST_CONFIGFILE_PATH,"isFirstLaunch");
            if (isFirstLaunch.equals("true")){
                return true;
            }else
                return false;

        } catch (Exception e) {
            log.error("读取R程序的配置文件失败,原因：{}",e);
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 修改配置文件
     */
    public void fixconfigFile() {
        try {
            String configJson = FileUtil.readJsonFromFile(RConstant.TEST_CONFIGFILE_PATH);
            JSONObject config = new JSONObject();
            config = JSON.parseObject(configJson);

            config.replace("isFirstLaunch",false);
            FileUtil.writeJsonToFile(RConstant.TEST_CONFIGFILE_PATH,config.toString());
        }catch (Exception e){
            log.error("修改配置文件失败,原因：{}",e);
            e.getMessage();
        }

    }

    /**
     * 将本程序设置为开机自启动
     */
    public void setAutoLunch() {

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
     * @param Command
     */
    public void executeCommand(String Command) {
        JSONObject commandJson = JSONObject.parseObject(Command);

        ExecuteCommand executeCommand = null;
        //没有需要执行的指令
        try {
            //查询成功，且有命令需要执行
            if (commandJson.get("resultCode").equals("0") && commandJson.getString("commandID") != null) {
                //下载jar包
                boolean jarDownloaded = downloadJar(commandJson);

                //实例化ExecuteCommand对象
                if(jarDownloaded){
                    executeCommand = (ExecuteCommand) FileUtil.loadObjectFromJar(commandJson.getString("jarPath"),commandJson.getString("classPath"));
                }else{
                    log.info("下载jar包失败");
                    return;
                }

                //执行相关操作
                if(executeCommand!=null){
                    //下载文件
                    try {
                        executeCommand.downloadFile(commandJson);
                    }catch (Exception e){
                        log.error("下载文件失败,原因：{}",e);
                    }

                    //关闭程序
                    try {
                        executeCommand.closeApp("appName");
                    }catch (Exception e){
                        log.error("关闭程序失败,原因：{}",e);
                    }


                    //安装更新包
                    try {
                        executeCommand.installApp(commandJson);
                    }catch (Exception e){
                        log.error("安装更新包失败,原因：{}",e);
                    }

                    //打开程序
                    try {
                        executeCommand.openApp(commandJson);
                    }catch (Exception e){
                        log.error("打开程序失败,原因：{}",e);
                    }

                    //执行命令
                    try {
                        executeCommand.execute();
                    }catch (Exception e){
                        log.error("执行命令失败,原因：{}",e);
                    }

                    //通知服务器
                    try {
                        executeCommand.noticeServer("schoolId");
                    }catch (Exception e){
                        log.error("通知服务器失败,原因：{}",e);
                    }

                }else {
                    log.info("ExecuteCommand对象为空");
                    return;
                }

            }else{
                log.info("没有需要执行的命令");
            }

        }catch (Exception e){
            log.error("执行命令发生异常");
            e.getMessage();
        }






    }

    /**
     * 下载jar包
     */
    private boolean downloadJar(JSONObject commandJson) {
        //1、根据jarPath判断是否本地是否已经下载了
        //1.1如本地下载了就根据jarHashCode判断jar包是否完整，否则删除原来的重新下载

        //2、根据jarUrl下载jar包到jarPath指定的路径
        //2.1 根据jarHashCode判断jar包是否完整
        try {
            String jarUrl = commandJson.getString("jarUrl");
            String jarPath = commandJson.getString("jarPath");
            WebUtils.download(jarUrl,jarPath);
            return true;
        }catch (Exception e){
            log.error("下载Jar包失败");
            e.getMessage();
        }

        return false;
    }


}//END
