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
        log.info("关闭程序："+ProgramUtils.shutdownProgram("Editor.exe"));
    }

    @Test
    public void startProgram() {
        try {
            ProgramUtils.startProgram("C:/Users/Administrator/AppData/Local/GitBook_Editor/app-7.0.12/Editor.exe");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}