package com.py.kafka.consumer;

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
public class KafkaConsumer {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(KafkaConsumer.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
