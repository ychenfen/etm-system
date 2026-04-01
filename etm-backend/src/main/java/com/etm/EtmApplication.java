package com.etm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.etm.modules.*.mapper")
@EnableAsync
public class EtmApplication {
    public static void main(String[] args) {
        SpringApplication.run(EtmApplication.class, args);
        System.out.println("高校外聘老师管理系统启动成功！");
    }
}
