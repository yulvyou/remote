package com.example.remote.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.SchemePortResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ProgramUtilsTest {

    @Test
    public void shutdownProgram() {
//        log.info("关闭程序："+ProgramUtils.shutdownProgram("Editor.exe"));
    }

    @Test
    public void startProgram() {
        try {
            ProgramUtils.startProgram("F:\\csum\\test.exe");
            log.info("程序是否打开："+ProgramUtils.findProcess("test.exe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void findProgram(){
        log.info("程序是否打开："+ProgramUtils.findProcess("Editor.exe"));

    }
}