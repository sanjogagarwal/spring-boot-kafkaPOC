package com.example.spring_boot_kafkaPOC;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

public class MirrorKafkaListenerUtils {

    private static final String mirrorContainerGroupSuffix = "_mirror";

    private static final String mirrorTopicPrefix = "kafka-atlas-scus-wus-ssl-stg-scus";

    public static KafkaListener getMirrorKafkaListener(KafkaListener kafkaListener) {
        return new KafkaListener() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return KafkaListener.class;
            }
            @Override
            public String id() {
                return kafkaListener.id();
            }
            @Override
            public String containerFactory() {
                return kafkaListener.containerFactory();
            }
            @Override
            public String[] topics() {
                //TODO
                return new String[]{mirrorTopicPrefix + kafkaListener.topics()[0]};
            }
            @Override
            public String topicPattern() {
                return kafkaListener.topicPattern();
            }
            @Override
            public TopicPartition[] topicPartitions() {
                return kafkaListener.topicPartitions();
            }
            @Override
            public String containerGroup() {
                return getMirrorContainerGroup(kafkaListener.containerGroup());
            }
            @Override
            public String errorHandler() {
                return kafkaListener.errorHandler();
            }
            @Override
            public String groupId() {
                return kafkaListener.groupId();
            }
            @Override
            public boolean idIsGroup() {
                return kafkaListener.idIsGroup();
            }
            @Override
            public String clientIdPrefix() {
                return kafkaListener.clientIdPrefix();
            }
            @Override
            public String beanRef() {
                return kafkaListener.beanRef();
            }
            @Override
            public String concurrency() {
                return kafkaListener.concurrency();
            }
            @Override
            public String autoStartup() {
                return kafkaListener.autoStartup();
            }
            @Override
            public String[] properties() {
                return kafkaListener.properties();
            }
            @Override
            public boolean splitIterables() {
                return kafkaListener.splitIterables();
            }
            @Override
            public String contentTypeConverter() {
                return kafkaListener.contentTypeConverter();
            }

            @Override
            public String batch() {
                return "";
            }

            @Override
            public String filter() {
                return "";
            }

            @Override
            public String info() {
                return "";
            }
        };
    }

    public static String getMirrorContainerGroup(String mainContainerGroup) {
        return mainContainerGroup + mirrorContainerGroupSuffix;
    }
}
