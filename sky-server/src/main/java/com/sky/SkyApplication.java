package com.sky;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * 腾讯云
 SecretId
 AKIDXt22vhHdrLgvzvjT6dyBJI5kIkEgvwBD

 SecretKey
 zlJdDjCa2OcjhhaFn9HF7pLirmT1UuAF
 *
 */

/**
 *
 * 阿里云
 AccessKey ID
 LTAI5tRjQCBPMP2GZZPEKBrq

 AccessKey Secret
 JQhI1PNfkNkW0yMxLhr8SQL4i9su1h
 *
 */

@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
public class SkyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkyApplication.class, args);
        log.info("server started");
    }



}
