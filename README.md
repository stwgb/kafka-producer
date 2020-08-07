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

- acks=all or acks=-1: Is the strongest available guarantee. The leader waits for acknowledging of all in-sync replicas and then
responses. Acks=all is necessary if you don't wont loose data. By applaying acks=all you als need to apply **min.insync.replicas**
to enable durablitiy. Common setting is min.insync.replicas=2. Then two ISR including the leader must acknowlege. When the 
replication.factor=3, acks=all and min.insync.replicas=2 you can tolerate one offline broker, if more brokers are offline the 
producer throws an exception.

### Idempotence

Making the producer idempotent solve the problem of duplicated messages through network error. The idempotent procucer
also sets up retries=Interger.Max\_VALUE, max.in.flight.requests=5 without losing the order and acks=all. (You can also set does
value manually to show the exact config in your application.)
To make the procucer idempotent set **ENABLE_IDEMPOTENCE_CONFIG** to "true".
