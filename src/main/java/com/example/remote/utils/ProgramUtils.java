package com.example.remote.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 程序工具类
 */
@Slf4j
public class ProgramUtils {
    /**
     * 关闭程序
     *
     * @param programName 需要关闭程序的名称（要带后缀，如 Editor.exe）
     */
    public static boolean shutdownProgram(String programName) throws Exception {

        try {
            String command = "taskkill /f /im " + programName;
            Runtime runtime = Runtime.getRuntime();
            BufferedReader br = new BufferedReader(new InputStreamReader(runtime.exec(command).getInputStream()));
            //StringBuffer b = new StringBuffer();
            String line = null;
            StringBuffer b = new StringBuffer();
            while ((line = br.readLine()) != null) {
                b.append(line + "\n");
            }
            return true;
        } catch (Exception e) {
            throw new Exception("关闭程序" + programName + "失败,原因：" + e.getMessage());
        }
    }

    /**
     * 启动程序，可以是exe程序也可以是bat脚本
     *
     * @param programPath 程序的全路径包括后缀（例如"C:/Users/Administrator/Desktop/run.bat"）
     * @throws IOException
     */
    public static void startProgram(String programPath) throws Exception {
        try {
            String programName = programPath.substring(programPath.lastIndexOf("/") + 1, programPath.lastIndexOf("."));
            List<String> list = new ArrayList<String>();
            list.add("cmd.exe");
            list.add("/c");
            list.add("start");
            list.add("\"" + programName + "\"");
            list.add("\"" + programPath + "\"");
            ProcessBuilder pBuilder = new ProcessBuilder(list);
            pBuilder.start();

        } catch (Exception e) {
            throw new Exception("打开程序" + programPath + "失败,原因：" + e.getMessage());
        }
    }


}
