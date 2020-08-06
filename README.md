# kafka-producer

## BasicKafkaProducer

A simple kafka producer build on three steps:

1. create producer properties with the only required properties
2. create producer
3. create a producer record with the data to send
4. send the data

## CallbackKafkaProducer

The CallbackKafkaProducer extends the BasicKafkaProducer by executing
a callback and logging the metadata if the record was send successfully
or logging an error in case if an error.

## CallbackKafkaProducerWithKey

This producer uses a key to send the messages with the same key to the same partition.
Kafka can only guarantee the ordering within a parition. If ordering is important you need 
to apply a key to your message. 
**Note:** By changing the number of partition the result of the calculation of the partition
will also change. Because the calculation depends on the number of partitions.

## AdvancedKafkaProducer

The AdvancedKafkaProducer has a shutdownhook which gets executed, when the producer stops.
You can take this hook to close connections and close itself to send the remaining messages.
   
### Acks

Acks enable durability garantuees:

- acks=0: No response of the broker is required -> data can be lost if broker goes down or producer throws an exeption.
If it is okay to lose messages acks=0 is fine.

- acks=1: The reponse of the leader broker is requested. But the broker will not wait for the acknowledging of the followers.
Messages get lost if leader goes down before the followers got the replications of the messages.

