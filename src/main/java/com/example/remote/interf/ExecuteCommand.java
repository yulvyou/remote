package com.example.remote.interf;


import com.alibaba.fastjson.JSONObject;

/**
 *
 */
public interface ExecuteCommand {

    //下载文件
    public boolean downloadFile(JSONObject commandJson);
    //关闭程序
    public boolean closeApp(JSONObject commandJson);
    //安装程序
    public boolean installApp(JSONObject commandJson);
    //执行打开程序
    public boolean openApp(JSONObject commandJson);
    //执行命令
    public boolean execute(JSONObject commandJson);

    //通知服务端完成更新
    public boolean noticeServer(JSONObject commandJson);
}
