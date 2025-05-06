package org.das.event_manager.config;

import org.das.event_manager.domain.EventChangeKafkaMessage;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration

public class KafkaConfig {

    @Bean
    public KafkaTemplate<Long, EventChangeKafkaMessage> kafkaTemplate(
            KafkaProperties kafkaProperties
    ) {
        var kafkaProps = kafkaProperties.buildProducerProperties(
                new DefaultSslBundleRegistry()
        );
        ProducerFactory<Long, EventChangeKafkaMessage> producerFactory =
            new DefaultKafkaProducerFactory<>(kafkaProps);
        return new KafkaTemplate<>(producerFactory);
    }
}
