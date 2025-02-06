package com.example.spring_boot_kafkaPOC;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.kafka.annotation.KafkaBootstrapConfiguration;
import org.springframework.kafka.config.KafkaListenerConfigUtils;

public class AtlasKafkaBootstrapConfiguration extends KafkaBootstrapConfiguration {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        super.registerBeanDefinitions(importingClassMetadata, registry);
        registry.registerBeanDefinition(KafkaListenerConfigUtils.KAFKA_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME,
                new RootBeanDefinition(CustomKafkaListenerAnnotationBeanPostProcessor.class));
    }
}
