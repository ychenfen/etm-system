package com.etm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.etm.mapper")
public class EtmApplication {
    public static void main(String[] args) {
        SpringApplication.run(EtmApplication.class, args);
    }
}
