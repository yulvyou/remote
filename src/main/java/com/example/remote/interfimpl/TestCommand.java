package com.example.remote.interfimpl;


import com.alibaba.fastjson.JSONObject;
import com.example.remote.constants.RConstant;
import com.example.remote.interf.ExecuteCommand;
import com.example.remote.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.util.List;

/**
 *
 */
@Slf4j
public class TestCommand implements ExecuteCommand {


    @Override
    public boolean downloadFile(JSONObject commandJson) throws Exception {

        try {
            log.info("进入downloadFile");
            //得到数据包的相关参数
            String packageUrl = commandJson.getString("packageUrl");
            String savePath = commandJson.getString("packagePath");
            String hashCode = commandJson.getString("packageHashCode");

            if (!CommonUtils.isNullOrBlank(packageUrl,savePath,hashCode)){//判断上列参数是否为null或者为“”
                //下载数据包
                boolean isDownloadFile = FileUtil.downloadFileAndCheck(packageUrl,savePath,hashCode);
                return isDownloadFile;
            }else {
                return false;
            }
        }catch (Exception e){
            throw new Exception("下载文件失败，原因："+e.getMessage());
        }

    }

    @Override
    public boolean closeApp(JSONObject commandJson) throws Exception {
        try {
            //直接关闭程序
            return ProgramUtils.shutdownProgram(commandJson.getString("programName"));
        }catch (Exception e){
            throw new Exception("关闭程序失败，原因："+e.getMessage());
        }

    }

    @Override
    public boolean installApp(JSONObject commandJson) throws Exception {
        try {
            //1、备份原来版本的文件到OldVersion中
            JSONObject detailJson = commandJson.getJSONObject("detail");
            //程序根路径
            String rootPath = commandJson.getString("rootPath");
            //存放备份的文件夹
            String feedbackDest = rootPath+"/OldVersion/"+detailJson.getString("preVersion");
            //需要备份的文件列表
            List feedbackFiles = commandJson.getJSONArray("backFiles");
            //更新包解压路径
            String unZipPath = commandJson.getString("updateProgramRootPath")+
                    "/package/";
            //更新包路径
            String packagePath = commandJson.getString("updateProgramRootPath")+
                    "/package/"+detailJson.getString("version");


            //开始备份
            boolean isFeedbacked = FileUtil.copyFileAndDirectoryToDirectory(feedbackDest,feedbackFiles,rootPath);

            //2、根据commandJson中的“datail-->files”字段替换文件，同时将“detail”字段写入config.json中
            //解压更新包
            boolean isUnZiped = FileUtil.unZip(commandJson.getString("packagePath"),unZipPath);

            List updateFiles = detailJson.getJSONArray("files");

            //增加或者替换文件
            try {
                boolean isReplaced = FileUtil.copyFileAndDirectoryToDirectory(rootPath,updateFiles,packagePath,true);
            }catch (Exception e){
                //TODO
                //版本回滚
                boolean isRollbacked = FileUtil.copyFileAndDirectoryToDirectory(rootPath,feedbackFiles,feedbackDest);
                //打开程序
                ProgramUtils.startProgram(commandJson.getString("launchPath"));
                throw new Exception("替换文件失败，原因："+e.getMessage());
            }
            return true;
        }catch (Exception e){
            throw new Exception("安装更新包失败，原因："+e.getMessage());
        }

    }

    @Override
    public boolean openApp(JSONObject commandJson) throws Exception {

        try {
            //1、运行测试文件，若成功则将配置文件config.json中的“isSuccessInstall”字段改为true
            //1.1运行测试文件


            //2、打开程序
            ProgramUtils.startProgram(commandJson.getString("launchPath"));
            boolean isLaunched = ProgramUtils.findProcess(commandJson.getString("programName"));

            if (!isLaunched){//如果程序打开失败就回滚
                JSONObject detailJson = commandJson.getJSONObject("detail");
                //程序根路径
                String rootPath = commandJson.getString("rootPath");
                //存放备份的文件夹
                String feedbackDest = rootPath+"/OldVersion/"+detailJson.getString("preVersion");
                //需要备份的文件列表
                List feedbackFiles = commandJson.getJSONArray("backFiles");
                //版本回滚
                boolean isRollbacked = FileUtil.copyFileAndDirectoryToDirectory(rootPath,feedbackFiles,feedbackDest);
                //打开程序
                ProgramUtils.startProgram(commandJson.getString("launchPath"));

            }else {
                //将detail字段的内容写入config.json文件中
                FileUtil.writeJsonToFile(commandJson.getString("rootPath")+"/config.json",commandJson.getString("detail"));
            }
            return true;
        } catch (Exception e) {
            throw new Exception("重启程序失败，原因："+e.getMessage());
        }
    }

    @Override
    public boolean execute(JSONObject commandJson) {
        //运行相关命令
        return true;
    }

    @Override
    public boolean noticeServer(JSONObject commandJson) throws Exception {
        try {
            JSONObject detailJson = commandJson.getJSONObject("detail");
            String feedbackUrl = RConstant.TEST_SERVER_URL+"/feedback?schoolId="+detailJson.getString("schoolID")+
                    "&clientNo="+detailJson.getString("clientNo")+"&isSuccess=true"+"&macAddr="+IpConfigUtils.getMACAddress();
            WebUtils.getJsonStrFromGetUrl(feedbackUrl);
            return true;
        }catch (Exception e){
            throw new Exception("通知服务端完成更新失败，原因："+e.getMessage());
        }

    }


}
