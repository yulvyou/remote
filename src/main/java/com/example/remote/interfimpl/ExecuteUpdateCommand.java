package com.example.remote.interfimpl;


import com.alibaba.fastjson.JSONObject;
import com.example.remote.interf.ExecuteCommand;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class ExecuteUpdateCommand implements ExecuteCommand {


    @Override
    public String downloadFile(JSONObject commandJson) {
        log.info("进入ExecuteUpdateCommand类中");
        //1、根据commandJson中的“packagePath”字段判断是否已经下载了package
        //1.1、根据“packageHashCode” 判断下载的package是否完整，若不完整则重新下载

        //2、根据packageUrl下载package到路径“packagePath”下
        //2.2、根据“packageHashCode” 判断下载的package是否完整
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void closeApp(String appName) {
        //直接关闭程序
    }

    @Override
    public void installApp(JSONObject commandJson) {
        //1、先判断程序是否关闭了，没有关闭就先关闭程序

        //2、备份原来版本的文件到OldVersion中

        //3、根据commandJson中的“datail-->files”字段替换文件，同时将“detail”字段写入config.json中
    }

    @Override
    public void openApp(JSONObject commandJson) {
        //1、运行测试文件，若成功则将配置文件config.json中的“isSuccessInstall”字段改为true

        //2、读取配置文件config.json中的“isSuccessInstall”字段，若为true则打开程序

    }

    @Override
    public void execute() {
        //运行相关命令
    }

    @Override
    public void noticeServer(String schoolId) {

    }
}
