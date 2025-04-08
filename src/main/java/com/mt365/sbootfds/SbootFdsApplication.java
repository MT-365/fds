package com.mt365.sbootfds;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mt365.sbootfds.mapper")
public class SbootFdsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbootFdsApplication.class, args);
    }

}
