package com.example.remote.interf;


import com.alibaba.fastjson.JSONObject;

/**
 *
 */
public interface ExecuteCommand {

    //下载文件
    public String downloadFile(JSONObject commandJson);
    //关闭程序
    public void closeApp(String appName);
    //安装程序
    public void installApp(JSONObject commandJson);
    //执行打开程序
    public void openApp(JSONObject commandJson);
    //执行命令
    public void execute();

    //通知服务端完成更新
    public void noticeServer(String schoolId);
}
