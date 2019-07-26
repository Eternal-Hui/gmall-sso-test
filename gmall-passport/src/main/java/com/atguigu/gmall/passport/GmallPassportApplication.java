package com.atguigu.gmall.passport;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.atguigu.gmall.passport.mapper")
public class GmallPassportApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallPassportApplication.class, args);
    }

}
