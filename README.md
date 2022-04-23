1. Install Kafka+Zookeeper and Storm.
2. Run Zookeeper, Kafka, and Storm on localhost.
3. Create topic my-first-topic with './bin/kafka-topics.sh --create --topic my-first-topic --bootstrap-server localhost:9092'
4. In the program folder, run 'mvn clean package -Dstorm.kafka.client.version=3.1.0 -Dcheckstyle.skip' to create a .jar file
5. In the Storm folder, run './bin/storm jar <path to KStorm>/target/storm-kafka-client-examples-2.4.0.jar KafkaStormSample > log.txt' to start the program
6. In the Zookeeper folder, run './bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic my-first-topic' to open the input console
7. Write some text in the console.
8. To finish program execution, write 'stopcount' in the console.
9. The results can be found in file 'storm_output.txt' in the program folder.
