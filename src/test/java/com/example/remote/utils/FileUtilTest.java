package com.example.remote.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.remote.constants.RConstant;
import com.example.remote.interf.ExecuteCommand;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.rmi.runtime.Log;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class FileUtilTest {

    @Test
    public void readJsonData() {
        try {
//            System.out.println(FileUtil.readJsonFromFile(RConstant.TEST_UPDATE_CONFIGFILE_PATH));


            String configJson = FileUtil.readJsonFromFile(RConstant.TEST_UPDATE_CONFIGFILE_PATH);
            JSONObject config = new JSONObject();
            config = JSON.parseObject(configJson);
            //TODO
            log.info(String.valueOf(config));
            config.replace("isFirstLaunch",false);
            FileUtil.writeJsonToFile(RConstant.TEST_UPDATE_CONFIGFILE_PATH,config.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void downloadFile(){
        WebUtils.download("http://localhost:8080/%E9%98%BF%E9%87%8C%E5%B7%B4%E5%B7%B4Java%E5%BC%80%E5%8F%91%E6%89%8B%E5%86%8C.pdf",
                "F:/ykt_test/download.pdf");
    }


    @Test
    public void loadObjectTest(){

        ExecuteCommand executeCommand = (ExecuteCommand) FileUtil.loadObjectFromJar("F:/ykt_test/testcommond.jar","com.example.remote.interfimpl.TestCommand");

//        executeCommand.closeApp("");
    }


    @Test
    public void copyDirectoryToDirectory(){
        String srcDir = "F:\\csum";
        String destDir = "F:\\ykt_test\\v1";
        FileUtil.copyDirectoryToDirectory(srcDir,destDir);
    }

    @Test
    public void copyFileTest(){
        String srcFile = "F:\\csum\\bin\\App.bat";
        String destFile ="F:\\ykt_test\\v1\\App.bat";
        FileUtil.copyFile(srcFile,destFile);
    }

    @Test
    public void getFileMD5CodeTest(){
        String filePath = "F:\\ykt_test\\commandtest.jar";
        log.info("MD5:"+FileUtil.getFileMD5Code(filePath));

    }

    @Test
    public void unZipTest(){
        String zipFilePath = "F:\\csum\\lib.zip";
        String destDir = "F:\\ykt_test\\v1";
        FileUtil.unZip(zipFilePath,destDir);
    }




}