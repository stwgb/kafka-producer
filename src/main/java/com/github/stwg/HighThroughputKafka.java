package com.github.stwg;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class HighThroughputKafka {
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(CallbackKafkaProducer.class);

        final String bootstrapServer = "localhost:9094";
        final String topic = "save-topic";

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Make the producer safe and apply exactly-once semantics
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));

        // High throughput settings

        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

        // If the use-case allows we wait for 30ms before sending the batch
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "30");
        // If batch size (32kB) is reached -> the batch will be send (even if we still linger)
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024));

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
