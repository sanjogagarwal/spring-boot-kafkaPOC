package com.example.spring_boot_kafkaPOC;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AliasFor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.ContainerGroupSequencer;
import org.springframework.messaging.handler.annotation.MessageMapping;

//{
//    "gdc010":{
//        "primaryCluster":{
//            "mirrorPrefix": "",
//            "broker": ""
//        },
//        "secondaryCluster":{
//            "mirrorPrefix": "",
//            "broker": ""
//        }
//    }
//}
//"atlas-kafka-is-primary-region-active": true
//"kafka-strategy":"mirror"

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@MessageMapping
@Documented
@KafkaListener(containerFactory = "containerFactory1")
public @interface CustomKafkaListener {

    String mirrorStrategy();

    @AliasFor(annotation = KafkaListener.class, attribute = "groupId")
    String groupId();

    @AliasFor(annotation = KafkaListener.class, attribute = "topics")
    String[] topics();

    @AliasFor(annotation = KafkaListener.class, attribute = "containerGroup")
    String containerGroup();
}


