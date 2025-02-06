package com.example.spring_boot_kafkaPOC;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerConfigUtils;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.support.LogIfLevelEnabled;

import java.util.Map;

@Configuration
public class KafkaConfig {

    public static final String FACTORY_1 = "containerFactory1";
//    @Autowired
//    private KafkaListenerEndpointRegistry registry;
//
//    @Bean(name = KafkaListenerConfigUtils.KAFKA_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
//    public KafkaListenerAnnotationBeanPostProcessor kafkaListenerAnnotationProcessor() {
//        KafkaListenerAnnotationBeanPostProcessor postProcessor = new CustomKafkaListenerAnnotationBeanPostProcessor();
//        postProcessor.setEndpointRegistry(registry);
//        return postProcessor;
//    }


    @Bean(name = FACTORY_1)
    public ConcurrentKafkaListenerContainerFactory<String, String>
    intermediateDcfinSalesPostingContainerFactory(
            @Qualifier("consumerFactory1")
            ConsumerFactory<String, String> consumerFactory,
            @Qualifier("commonErrorHandler")
            ErrorHandler commonErrorHandler) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(3);

        factory.getContainerProperties().setLogContainerConfig(true);
        factory.getContainerProperties().setCommitLogLevel(LogIfLevelEnabled.Level.INFO);
        factory.getContainerProperties().setSyncCommits(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

//        factory.setErrorHandler(commonErrorHandler);

        return factory;
    }

    @Bean(name ="consumerFactory1")
    public ConsumerFactory<String, String> intermediateDcfinSalesPostingConsumerFactory(
            @Qualifier("kafkaConsumerProperties1")
            KafkaProperties kafkaProperties,
            @Qualifier("commonKafkaProperties")
            KafkaProperties commonKafkaProperties) {

        Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties();
        Map<String, Object> commonConsumerProperties = commonKafkaProperties.buildConsumerProperties();
        if (commonConsumerProperties != null && !commonConsumerProperties.isEmpty()) {
            commonConsumerProperties.forEach(
                    (k, v) -> {
                        consumerProperties.putIfAbsent(k, v);
                    });
        }
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(consumerProperties);
    }

    @Bean(name ="kafkaConsumerProperties1")
    @ConfigurationProperties("consumer1.spring.kafka")
    public KafkaProperties intermediateEventConsumerKafkaProperties() {
        return new KafkaProperties();
    }

    @Bean(name = "commonKafkaProperties")
    @ConfigurationProperties("common.spring.kafka")
    public KafkaProperties commonKafkaProperties() {
        return new KafkaProperties();
    }

    @Bean(name = "commonErrorHandler")
    public ErrorHandler commonErrorHandler() {
        ErrorHandler errorHandler = new ErrorHandler() {
            @Override
            public void handle(Exception thrownException, ConsumerRecord<?, ?> data) {
                System.out.println(thrownException);
            }
        };
        return errorHandler;
    }
}
