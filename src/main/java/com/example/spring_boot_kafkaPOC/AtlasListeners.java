package com.example.spring_boot_kafkaPOC;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Component;

@EnableKafka
@Component
public class AtlasListeners {

    @CustomKafkaListener(mirrorStrategy = "mirror", groupId = "KAFKA_LISTENER_INVENTORY_EP_GDC_PERF_POC", topics = "ATLAS_INVENTORY_STATUS_CORE_PERF", containerGroup = "customKafkaListener")
    public void listen(String data) {
        System.out.println("Message received" + data);
    }
}
