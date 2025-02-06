package com.example.spring_boot_kafkaPOC;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.ContainerGroupSequencer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CustomKafkaListenerAnnotationBeanPostProcessor extends KafkaListenerAnnotationBeanPostProcessor {

    ConfigurableApplicationContext appContext;

    @Autowired
    KafkaListenerEndpointRegistry registry;

    @Override
    protected void processKafkaListener(KafkaListener kafkaListener, Method method, Object bean, String beanName) {
        CustomKafkaListener customKafkaListener = method.getAnnotation(CustomKafkaListener.class);
        String mirrorStrategy = customKafkaListener.mirrorStrategy();
        if(mirrorStrategy.equals("mirror")) {
            handleMirrorStrategy(kafkaListener, method, bean, beanName);
        }
        super.processKafkaListener(kafkaListener, method, bean, beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        super.setApplicationContext(applicationContext);
        this.appContext = (ConfigurableApplicationContext) applicationContext;
    }

    private void handleMirrorStrategy(KafkaListener kafkaListener, Method method, Object bean, String beanName) {
        registerSequencerBean(kafkaListener);
        KafkaListener mirrorKafkaListener = MirrorKafkaListenerUtils.getMirrorKafkaListener(kafkaListener);
        super.processKafkaListener(mirrorKafkaListener, method, bean, beanName);
    }

    private void registerSequencerBean(KafkaListener kafkaListener) {
        String mirrorContainerGroup = MirrorKafkaListenerUtils.getMirrorContainerGroup(kafkaListener.containerGroup());
        ContainerGroupSequencer groupSequencer = new ContainerGroupSequencer(registry, 5000, mirrorContainerGroup, kafkaListener.containerGroup());
        appContext.getBeanFactory().registerSingleton(kafkaListener.containerGroup(), groupSequencer);
    }
}
