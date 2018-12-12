package com.example.remote.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
            File file = new File(filePath);
            FileUtils.writeStringToFile(file,json,"UTF-8");
        }catch (Exception e){
            log.error("写入文件：{} 失败",filePath);
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


    /**
     * 文件复制，若目的文件没有则创建
     * @param srcFile 源文件
     * @param destFile 目标文件
     * @return
     */
    public static boolean copyFile(String srcFile, String destFile){
        try {
            File src =new File(srcFile);
            File dest =new File(destFile);
            FileUtils.copyFile(src,dest);
            return true;
        }catch (Exception e){
            log.error("文件 {} 拷贝失败,原因：{}",srcFile,e);
            return false;
        }
    }



    /**
     * 复制文件夹里面的内容到新的文件夹，如果新文件夹不存在自动创建
     * @param srcDir
     * @param destDir
     * @return
     */
    public static boolean copyDirectory(String srcDir, String destDir){

        try {
            File src =new File(srcDir);
            File dest =new File(destDir);
            FileUtils.copyDirectory(src,dest);
            return true;
        }catch (Exception e){
            log.error("目录 {} 拷贝失败,原因：{}",srcDir,e);
            return false;
        }
    }

    /**
     * 拷贝整个文件夹到新的文件夹,如果新文件夹不存在自动创建
     * @param srcDir
     * @param destDir
     * @return
     */
    public static boolean copyDirectoryToDirectory(String srcDir, String destDir){
        try {
            File src =new File(srcDir);
            File dest =new File(destDir);
            FileUtils.copyDirectoryToDirectory(src,dest);
            return true;
        }catch (Exception e){
            log.error("目录 {} 拷贝失败,原因：{}",srcDir,e);
            return false;
        }
    }

    /**
     * 获取文件 MD5 code
     * @param filePath 文件路径
     * @return
     */
    public static String getFileMD5Code(String filePath){
        try {
            FileInputStream fis= new FileInputStream(filePath);
            String md5Code = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
            IOUtils.closeQuietly(fis);
            return md5Code;
        }catch (Exception e){
            log.error("获取文件 {} 的MD5Code失败,原因：{}",filePath,e);
            return null;
        }

    }


    /**
     * 解压zip 文件
     * @param zipFile 需要解压的zip文件
     * @param destDir 解压后保存的目录
     * @return
     */
    public static boolean unZip(String zipFile, String destDir) {
        File f = null;
        try (ArchiveInputStream i = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, Files.newInputStream(Paths.get(zipFile)))) {
            ArchiveEntry entry = null;
            while ((entry = i.getNextEntry()) != null) {
                if (!i.canReadEntryData(entry)) {
                    continue;
                }
                f = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    if (!f.isDirectory() && !f.mkdirs()) {
                        throw new IOException("failed to create directory " + f);
                    }
                } else {
                    File parent = f.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("failed to create directory " + parent);
                    }
                    try (OutputStream o = Files.newOutputStream(f.toPath())) {
                        IOUtils.copy(i, o);
                    }
                }
            }
            return true;
        } catch (IOException | ArchiveException e) {

            log.error("解压文件 {} 失败,原因：{}",zipFile,e);
            return false;
        }

    }




}
