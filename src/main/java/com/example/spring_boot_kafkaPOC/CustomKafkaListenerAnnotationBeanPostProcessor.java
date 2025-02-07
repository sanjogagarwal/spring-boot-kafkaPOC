package com.example.spring_boot_kafkaPOC;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.kafka.config.KafkaListenerConfigUtils;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.ContainerGroupSequencer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component(KafkaListenerConfigUtils.KAFKA_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
@Primary
public class CustomKafkaListenerAnnotationBeanPostProcessor extends KafkaListenerAnnotationBeanPostProcessor {

    ConfigurableApplicationContext appContext;

    @Autowired
    KafkaListenerEndpointRegistry registry;

    @Override
    protected void processKafkaListener(KafkaListener kafkaListener, Method method, Object bean, String beanName) {
        CustomKafkaListener customKafkaListener = (CustomKafkaListener) method.getDeclaredAnnotations()[0];
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
        ContainerGroupSequencer groupSequencer = new ContainerGroupSequencer(registry, 25000, mirrorContainerGroup, kafkaListener.containerGroup());
        groupSequencer.setApplicationContext(appContext);
        appContext.getBeanFactory().registerSingleton(kafkaListener.containerGroup()+"_sequencer", groupSequencer);
        appContext.addApplicationListener(groupSequencer);
    }
}
