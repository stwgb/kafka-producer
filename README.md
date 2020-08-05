# kafka-producer

## BasicKafkaProducer

A simple kafka producer build on three steps:

1. create producer properties with the only required properties
2. create producer
3. create a producer record with the data to send
4. send the data

## CallbackKafkaProducer

The CallbackKafkaProducer enhences the BasicKafkaProducer by executing
a callback and logging the metadata if the record was send successfully
or logging an error in case if an error.
