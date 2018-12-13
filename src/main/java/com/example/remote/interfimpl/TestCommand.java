package com.example.remote.interfimpl;


import com.alibaba.fastjson.JSONObject;
import com.example.remote.interf.ExecuteCommand;
import com.example.remote.utils.CommonUtils;
import com.example.remote.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class TestCommand implements ExecuteCommand {


    @Override
    public boolean downloadFile(JSONObject commandJson) {
        log.info("进入downloadFile");

        String packageUrl = commandJson.getString("packageUrl");
        String savePath = commandJson.getString("packagePath");
        String hashCode = commandJson.getString("packageHashCode");

        if (!CommonUtils.isNullOrBlank(packageUrl,savePath,hashCode)){
            //下载数据包
            boolean isDownloadFile = FileUtil.downloadFileAndCheck(packageUrl,savePath,hashCode);
            return isDownloadFile;
        }else {
            return false;
        }
    }

    @Override
    public boolean closeApp(JSONObject commandJson) {
        //直接关闭程序

        return true;
    }

    @Override
    public boolean installApp(JSONObject commandJson) {
        //1、先判断程序是否关闭了，没有关闭就先关闭程序

        //2、备份原来版本的文件到OldVersion中

        //3、根据commandJson中的“datail-->files”字段替换文件，同时将“detail”字段写入config.json中
        return true;
    }

    @Override
    public boolean openApp(JSONObject commandJson) {
        //1、运行测试文件，若成功则将配置文件config.json中的“isSuccessInstall”字段改为true

        //2、读取配置文件config.json中的“isSuccessInstall”字段，若为true则打开程序
        return true;
    }

    @Override
    public boolean execute(JSONObject commandJson) {
        //运行相关命令
        return true;
    }

    @Override
    public boolean noticeServer(JSONObject commandJson) {
        return true;
    }
}
