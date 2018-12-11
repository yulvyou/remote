package com.example.remote.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 读取json文件工具类
 */
@Slf4j
public class FileUtil {

    /**
     * 读取json文件并且转换成字符串
     * @param filePath 文件的路径
     * @return
     */
    public static String readJsonFromFile(String filePath) {

        StringBuffer strbuffer = new StringBuffer();
        File myFile = new File(filePath);
        if (!myFile.exists()) {
            log.error("文件：{}，不存在",filePath);
        }
        try {
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
            BufferedReader in  = new BufferedReader(inputStreamReader);

            String str;
            while ((str = in.readLine()) != null) {
                strbuffer.append(str);
            }
            in.close();
            fis.close();
        } catch (IOException e) {
            log.error("读取文件：{} 失败",filePath);
            e.getStackTrace();
        }
        return strbuffer.toString();
    }


    /**
     * 将Json写入文件（覆盖原文件中的内容）
     * @param filePath
     * @param json
     */
    public static void writeJsonToFile(String filePath,String json){
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(json.getBytes());
            fos.close();
        }catch (Exception e){
            log.error("写入文件：{} 失败",filePath);
            e.printStackTrace();
        }
    }

    /**
     * 得到json文件中的某个key的value值
     * @param filePath json文件路径
     * @param key 需要查询的key
     * @return
     */
    public static String getKeyFromJsonFile(String filePath,String key){
        try {
            String content = readJsonFromFile(filePath);

            JSONObject config = new JSONObject();
            config = JSON.parseObject(content);
            //TODO
//            log.info(String.valueOf(config));

            String result = config.getString(key);

            return result;
        }catch (Exception e){
            log.error("从文件 {} 中读取 {} 字段失败",filePath,key);
            return null;
        }
    }

    /**
     * 通过jar包加载指定类
     * @param jarPath jar包路径
     * @param loadClassPath 指定类（需要带上包名）
     * @return
     */
    public static Object loadObjectFromJar(String jarPath,String loadClassPath){
        Object obj = null;
        try {
            File file=new File(jarPath);//类路径(包文件上一层)
            URL url=file.toURI().toURL();
            ClassLoader loader=new URLClassLoader(new URL[]{url});//创建类加载器

            Class<?> cls=loader.loadClass(loadClassPath);//加载指定类，注意一定要带上类的包名
            obj=cls.newInstance();//初始化一个实例
            return obj;
        }catch (Exception e){
            log.error("加载{}jar包中的{}类失败",jarPath,loadClassPath);
        }
        return obj;
    }

}
