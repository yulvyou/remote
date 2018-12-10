package com.example.remote.factory;


import com.example.remote.interf.ExecuteCommand;

/**
 * 命令工厂
 */
public class CommandFactory {
    public static ExecuteCommand getInstance(String className ){
        ExecuteCommand executeCommand = null;

        try {
            executeCommand = (ExecuteCommand) Class.forName(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  executeCommand;
    }
}
