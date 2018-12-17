package com.example.remote.interf;


import com.alibaba.fastjson.JSONObject;

/**
 *
 */
public interface ExecuteCommand {

    //下载文件
    public boolean downloadFile(JSONObject commandJson) throws Exception;
    //关闭程序
    public boolean closeApp(JSONObject commandJson) throws Exception;
    //安装程序
    public boolean installApp(JSONObject commandJson) throws Exception;
    //执行打开程序
    public boolean openApp(JSONObject commandJson) throws Exception;
    //执行命令
    public boolean execute(JSONObject commandJson)throws Exception;

    //通知服务端完成更新
    public boolean noticeServer(JSONObject commandJson) throws Exception;
}
