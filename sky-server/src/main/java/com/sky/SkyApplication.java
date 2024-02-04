package com.sky;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
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
/**
 小程序id：wxcf848b8acb49d37f
 密钥：cd92d5c6e8c6e1bbbebc47f398b9b659
 *
 */


@SpringBootApplication
@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
@EnableCaching //开启缓存注解功能
@EnableScheduling //开启定时处理功能
public class SkyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkyApplication.class, args);
        log.info("server started");
    }



}
