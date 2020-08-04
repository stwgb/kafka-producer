package com.github.stwg;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class BasicKafkaProducer {
    public static void main(String[] args) {

        final String bootstrapServer = "localhost:9094";
        final String topic = "basic-kafka-producer-topic";

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> kafkaProducer= new KafkaProducer<>(properties);

        ProducerRecord<String, String> producerRecord =
                new ProducerRecord<>(topic,"hello from BasicKafkaProducer");

        // async, so flush and close is required
        kafkaProducer.send(producerRecord);

        kafkaProducer.flush();
        kafkaProducer.close();

    }
}
