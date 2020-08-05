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


