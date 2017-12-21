package com.py.kafka.producer;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

/**
 * 应用程序入口
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableCaching
@ComponentScan(basePackages = "com.py")
public class KafkaProducer {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(KafkaProducer.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
