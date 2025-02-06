package com.example.spring_boot_kafkaPOC;

public class AtlasListeners {

    @CustomKafkaListener(mirrorStrategy = "mirror", groupId = "customKafkaListener", topics = "ATLAS_INVENTORY_STATUS_CORE_PERF", containerGroup = "customKafkaListener")
    public void listen(String data) {
        System.out.println("Message received" + data);
    }
}
