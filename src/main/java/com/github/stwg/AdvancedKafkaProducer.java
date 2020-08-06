package com.github.stwg;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class AdvancedKafkaProducer {
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(CallbackKafkaProducer.class);

        final String bootstrapServer = "localhost:9094";
        final String topic = "advanced-kafka-producer-topic";

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        for (int i = 0; i <= 5; i++) {

            String message = "hello from CallbackKafkaProducer: " + i;
            String key = "id_" + i;

            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<>(topic, key, message);

            // async, so flush and close is required
            kafkaProducer.send(producerRecord, (metadata, exception) -> {
                if (exception == null) {
                    logger.info("Received metadata: \n"
                            + ">>> Topic: " + metadata.topic() + "\n"
                            + ">>> Partition: " + metadata.partition() + "\n"
                            + ">>> Offset: " + metadata.offset() + "\n"
                            + ">>> Timestamp: " + metadata.timestamp()
                    );
                } else {
                    logger.error("Error while producing data", exception);
                }
            });
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("shutdown kafka producer");
            kafkaProducer.close();
        } ));
    }
}
