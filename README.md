kafka-demo
===
## 项目介绍
将kafka的封装、消息的生成与消费整合到一个项目中，项目为springboot项目，使用maven进行多模块整合。

## 项目模块
- kafka-base
- kafka-producer
- kafka-consumer

## 各模块介绍
### kafka-base
- base包:公用基础类 
- kafka包:kafka相关类封装与实现
- proto包:Message.proto文件(需要与protobuf插件一起使用)
- pom.xml：
```
<properties>
        <grpc.version>1.6.1</grpc.version>
        <protobuf.version>3.3.0</protobuf.version>
    </properties>

    <dependencies>
        <!--proto相关start-->
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-netty</artifactId>
            <version>${grpc.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-protobuf</artifactId>
            <version>${grpc.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-stub</artifactId>
            <version>${grpc.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.google.protobuf</groupId>
            <artifactId>protobuf-java</artifactId>
            <version>${protobuf.version}</version>
        </dependency>
        <!--proto相关end-->

        <dependency>
            <groupId>org.apache.kafka</groupId>
            <artifactId>kafka-clients</artifactId>
            <version>0.10.0.1</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.5</version>
        </dependency>
    </dependencies>

    <build>
        <extensions>
            <extension>
                <groupId>kr.motd.maven</groupId>
                <artifactId>os-maven-plugin</artifactId>
                <version>1.5.0.Final</version>
            </extension>
        </extensions>
        <plugins>
            <plugin>
                <groupId>org.xolstice.maven.plugins</groupId>
                <artifactId>protobuf-maven-plugin</artifactId>
                <version>0.5.0</version>
                <configuration>
                    <protocArtifact>com.google.protobuf:protoc:${protobuf.version}:exe:${os.detected.classifier}</protocArtifact>
                    <pluginId>grpc-java</pluginId>
                    <pluginArtifact>io.grpc:protoc-gen-grpc-java:${grpc.version}:exe:${os.detected.classifier}</pluginArtifact>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>compile-custom</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```
### kafka-producer
- service:生产者服务类
- KafkaProducer:生产者启动类
### kafka-consumer
- service:消费者服务类
- KafkaConsumer:消费者启动类

## 项目启动
- 将ProducerService和ConsumerService中 "xx.xx.x.xxx:9092" 换成正确kafka地址(只是个demo，故许多配置都是硬编码，未使用配置文件)
- 启动KafkaProducer类
- 启动kafkaConsumer类

## 结果预测
- kafka-producer:一直输出 1000rspToMessage
```
1000rspToMessage
1000rspToMessage
1000rspToMessage
1000rspToMessage
1000rspToMessage
1000rspToMessage
```
- kafka-consumer:一直输出 1000reqToMessage
```
1000reqToMessage
1000reqToMessage
1000reqToMessage
1000reqToMessage
1000reqToMessage
1000reqToMessage
```


