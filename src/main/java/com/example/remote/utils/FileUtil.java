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
    public static String readJsonFromFile(String filePath) throws Exception {
        try {
            StringBuffer strbuffer = new StringBuffer();
            File myFile = new File(filePath);
            if (!myFile.exists()) {
                log.error("文件：{}，不存在",filePath);
            }
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
            BufferedReader in  = new BufferedReader(inputStreamReader);

            String str;
            while ((str = in.readLine()) != null) {
                strbuffer.append(str);
            }
            in.close();
            fis.close();
            return strbuffer.toString();
        } catch (IOException e) {
            throw new Exception("读取文件"+filePath+"失败,原因："+e.getMessage());
        }

    }


    /**
     * 将Json写入文件（覆盖原文件中的内容）
     * @param filePath
     * @param json
     */
    public static void writeJsonToFile(String filePath,String json) throws Exception {
        try {
            File file = new File(filePath);
            FileUtils.writeStringToFile(file,json,"UTF-8");
        }catch (Exception e){
            throw new Exception("写入文件"+filePath+"失败,原因："+e.getMessage());
        }
    }

    /**
     * 得到json文件中的某个key的value值
     * @param filePath json文件路径
     * @param key 需要查询的key
     * @return
     */
    public static String getKeyFromJsonFile(String filePath,String key) throws Exception {
        try {
            String content = readJsonFromFile(filePath);

            JSONObject config = new JSONObject();
            config = JSON.parseObject(content);

            String result = config.getString(key);

            return result;
        }catch (Exception e){
            throw new Exception("从文件"+filePath+"中获取"+key+"值失败,原因："+e.getMessage());
        }
    }

    /**
     * 通过jar包加载指定类
     * @param jarPath jar包路径
     * @param loadClassPath 指定类（需要带上包名）
     * @return
     */
    public static Object loadObjectFromJar(String jarPath,String loadClassPath) throws Exception {
        try {
            Object obj = null;
            File file=new File(jarPath);//类路径(包文件上一层)
            URL url=file.toURI().toURL();
            ClassLoader loader=new URLClassLoader(new URL[]{url});//创建类加载器

            Class<?> cls=loader.loadClass(loadClassPath);//加载指定类，注意一定要带上类的包名
            obj=cls.newInstance();//初始化一个实例
            return obj;
        }catch (Exception e){
            throw new Exception("加载"+jarPath +"包中的"+loadClassPath+"类失败,原因："+e.getMessage());
        }
    }


    /**
     * 文件复制，若目的文件没有则创建
     * @param srcFile 源文件
     * @param destFile 目标文件或者文件夹
     * @return
     */
    public static boolean copyFile(String srcFile, String destFile) throws Exception {
        try {
            File src =new File(srcFile);
            File dest =new File(destFile);
            if (dest.isDirectory()){
                FileUtils.copyFileToDirectory(src,dest);
            }else {
                FileUtils.copyFile(src,dest);
            }
            return true;
        }catch (Exception e){
            throw new Exception("文件"+srcFile+"拷贝失败,原因："+e.getMessage());
        }
    }



    /**
     * 复制文件夹里面的内容到新的文件夹，如果新文件夹不存在自动创建
     * @param srcDir
     * @param destDir
     * @return
     */
    public static boolean copyDirectory(String srcDir, String destDir) throws Exception {
        try {
            File src =new File(srcDir);
            File dest =new File(destDir);
            FileUtils.copyDirectory(src,dest);
            return true;
        }catch (Exception e){
            throw new Exception("目录"+srcDir+"拷贝失败,原因："+e.getMessage());
        }
    }

    /**
     * 拷贝整个文件夹到新的文件夹,如果新文件夹不存在自动创建
     * @param srcDir
     * @param destDir
     * @return
     */
    public static boolean copyDirectoryToDirectory(String srcDir, String destDir) throws Exception {
        try {
            File src =new File(srcDir);
            File dest =new File(destDir);
            FileUtils.copyDirectoryToDirectory(src,dest);
            return true;
        }catch (Exception e){
            throw new Exception("目录"+srcDir+"拷贝失败,原因："+e.getMessage());
        }
    }


    /**
     * 拷贝文件或者文件夹
     * @param destDir 目标文件夹
     * @param srcFileOrDirs 需要复制的文件列表
     * @param rootPath 需要复制的文件的根路径
     * @return
     */
    public static boolean copyFileAndDirectoryToDirectory( String destDir,List<String> srcFileOrDirs,String rootPath) throws Exception {
        try {
            for (String srcFileOrDir:srcFileOrDirs ) {
                String srcPath = rootPath+srcFileOrDir;
                String destPath = destDir+srcFileOrDir;
                File src =new File(srcPath);
                if(src.isDirectory()){
                    FileUtil.copyDirectoryToDirectory(srcPath,destPath);
                }else {
                    FileUtil.copyFile(srcPath,destPath);
                }
            }
            return true;
        }catch (Exception e){
            throw new Exception("拷贝文件"+srcFileOrDirs.toString()+"失败,原因："+e.getMessage());
        }

    }



    /**
     * 获取文件 MD5 code
     * @param filePath 文件路径
     * @return
     */
    public static String getFileMD5Code(String filePath) throws Exception {
        try {
            FileInputStream fis= new FileInputStream(filePath);
            String md5Code = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
            IOUtils.closeQuietly(fis);
            return md5Code;
        }catch (Exception e){
            throw new Exception("获取"+filePath+"的MD5Code失败,原因："+e.getMessage());
        }

    }


    /**
     * 解压zip 文件
     * @param zipFile 需要解压的zip文件
     * @param destDir 解压后保存的目录
     * @return
     */
    public static boolean unZip(String zipFile, String destDir) throws Exception {

        try (ArchiveInputStream i = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, Files.newInputStream(Paths.get(zipFile)))) {
            File f = null;
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
        } catch (Exception e) {
            throw new Exception("解压文件"+zipFile+"失败,原因："+e.getMessage());
        }
    }


    /**
     * 判断文件是否存在且完整
     * @param filePath 文件路径
     * @param hashCode 文件的MD5Code
     * @return
     */
    public static boolean isExistsAndComplete(String filePath, String hashCode) throws Exception {
        try {
            File file = new File(filePath);

            //如果不存在返回false
            if(!file.exists())
                return false;

            //判断文件是否完整
            if (FileUtil.getFileMD5Code(filePath).equals(hashCode)){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            throw new Exception("判断文件"+filePath+"是否存在且完整失败,原因："+e.getMessage());
        }

    }


    /**
     * 下载和校验文件是否完整
     * @param url 文件下载地址
     * @param savePath 文件保存地址
     * @param hashCode MD5Code
     * @return
     */
    public static boolean downloadFileAndCheck(String url, String savePath, String hashCode) throws Exception {
        try {
            //1、根据savePath判断是否本地是否已经下载且是完整的
            boolean isExistsAndComplete = FileUtil.isExistsAndComplete(savePath,hashCode);
            if(!isExistsAndComplete){
                //2、根据Url下载文件到 savePath 指定的路径
                WebUtils.download(url,savePath);
                //2.1 根据 hashCode 判断文件是否完整
                String fileMD5Code = FileUtil.getFileMD5Code(savePath);
                if (fileMD5Code.equals(hashCode)){
                    return true;
                }else {
                    return false;
                }

            }else{
                return true;
            }
        }catch (Exception e){
            throw new Exception("下载文件"+url+"失败,原因："+e.getMessage());
        }
    }
}
