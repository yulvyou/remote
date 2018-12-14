package com.example.remote.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.remote.constants.RConstant;
import com.example.remote.interf.ExecuteCommand;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JsonbTester;
import org.springframework.test.context.junit4.SpringRunner;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class FileUtilTest {

//    @Test
//    public void readJsonData() {
//        try {
////            System.out.println(FileUtil.readJsonFromFile(RConstant.TEST_UPDATE_CONFIGFILE_PATH));
//
//
//            String configJson = FileUtil.readJsonFromFile(RConstant.TEST_UPDATE_CONFIGFILE_PATH);
//            JSONObject config = new JSONObject();
//            config = JSON.parseObject(configJson);
//            //TODO
//            log.info(String.valueOf(config));
//            config.replace("isFirstLaunch",false);
//            FileUtil.writeJsonToFile(RConstant.TEST_UPDATE_CONFIGFILE_PATH,config.toString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    @Test
//    public void downloadFile(){
//        WebUtils.download("http://localhost:8080/%E9%98%BF%E9%87%8C%E5%B7%B4%E5%B7%B4Java%E5%BC%80%E5%8F%91%E6%89%8B%E5%86%8C.pdf",
//                "F:/ykt_test/download.pdf");
//    }
//
//
//    @Test
//    public void loadObjectTest(){
//
//        ExecuteCommand executeCommand = (ExecuteCommand) FileUtil.loadObjectFromJar("F:/ykt_test/testcommond.jar","com.example.remote.interfimpl.TestCommand");
//
////        executeCommand.closeApp("");
//    }
//
//
//    @Test
//    public void copyDirectoryToDirectory(){
//        String srcDir = "F:\\csum";
//        String destDir = "F:\\ykt_test\\v1";
//        FileUtil.copyDirectoryToDirectory(srcDir,destDir);
//    }
//
//    @Test
//    public void copyFileTest(){
//        String srcFile = "F:\\csum\\bin\\App.bat";
//        String destFile ="F:\\ykt_test\\v1\\App.bat";
//        FileUtil.copyFile(srcFile,destFile);
//    }
//
//    @Test
//    public void getFileMD5CodeTest(){
//        String filePath = "F:\\Projects\\Test\\MethodTest\\src\\main\\resources\\static\\v2.zip";
////        log.info("MD5:"+FileUtil.getFileMD5Code(filePath));
//
//    }
//
//    @Test
//    public void unZipTest(){
//        String zipFilePath = "F:\\csum\\lib.zip";
//        String destDir = "F:\\ykt_test\\v1";
//        FileUtil.unZip(zipFilePath,destDir);
//    }
//
//    @Test
//    public void copyFileAndDirectoryToDirectoryTest(){
//        String desc = "F:\\csum\\OldVersion\\v2";
//        String rootPath = "F:\\csum\\";
//        List<String> list = new ArrayList();
//        list.add("bin");
//        list.add("config.json");
//        list.add("lib");
//
//        FileUtil.copyFileAndDirectoryToDirectory(desc,list,rootPath);
//
//
//    }
//
//
//
//
//    @Test
//    public void jsonTest(){
//        String json = "{\n" +
//                "\"name\":\"网站\",\n" +
//                "\"num\":3,\n" +
//                "\"sites\":[ \"Google\", \"Runoob\", \"Taobao\" ]\n" +
//                "}";
//        JSONObject jsonObject = JSONObject.parseObject(json);
//
//        List sites = jsonObject.getJSONArray("sites");
//        log.info(sites.size() + "");
//
//    }


}