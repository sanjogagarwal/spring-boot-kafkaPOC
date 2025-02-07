package com.example.spring_boot_kafkaPOC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaBootstrapConfiguration;

@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class SpringBootKafkaPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootKafkaPocApplication.class, args);
	}

}
