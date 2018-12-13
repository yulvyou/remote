package com.example.remote.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CommonUtilsTest {

    @Test
    public void isNullOrBlank() {

        String str1 = null;
        String str2 = "";
        String str3 = "hell";
        String str4 = "good";
        log.info("是否为空："+CommonUtils.isNullOrBlank(str1,str2));
        log.info("是否为空："+CommonUtils.isNullOrBlank(str1,str2,str3));
        log.info("是否为空："+CommonUtils.isNullOrBlank(str1,str3));
        log.info("是否为空："+CommonUtils.isNullOrBlank(str2,str3));
        log.info("是否为空："+CommonUtils.isNullOrBlank(str3,str4,str1));
        log.info("是否为空："+CommonUtils.isNullOrBlank(str3,str4,str2));
        log.info("是否为空："+CommonUtils.isNullOrBlank(str3,str4));

    }
}