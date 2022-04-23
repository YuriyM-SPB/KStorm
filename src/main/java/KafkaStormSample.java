import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.spout.FirstPollOffsetStrategy;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

public class KafkaStormSample {
    public static void main(String[] args) throws Exception{
        Config config = new Config();
        config.setDebug(true);
        String zkConnString = "localhost:9092";
        String topic = "my-first-topic";

        
        KafkaSpoutConfig<String, String> kafkaSpoutConfig = KafkaSpoutConfig
                .builder(zkConnString,topic)
                .setProp(ConsumerConfig.GROUP_ID_CONFIG, "kafkaSpoutTestGroup")
                .setFirstPollOffsetStrategy(FirstPollOffsetStrategy.UNCOMMITTED_EARLIEST)
                .build();

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafka-spout", new KafkaSpout<>(kafkaSpoutConfig));
        builder.setBolt("word-spitter", new SplitBolt()).shuffleGrouping("kafka-spout");
        builder.setBolt("word-counter", new CountBolt()).shuffleGrouping("word-spitter");

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("KafkaStormSample", config, builder.createTopology());
        

        Writer fileWriter = new FileWriter("/tmp/stopper.txt", false);
        
        while(true){
        	Scanner txtscan = new Scanner(new File("/tmp/stopper.txt"));
        	while(txtscan.hasNextLine()){
        	    String str = txtscan.nextLine();
        	    if(str.indexOf("stopcount") != -1){
        	        System.out.println("EXIT");
        	        cluster.shutdown();
        	        System.exit(0);
        	    }
        	}
        	Thread.sleep(5);
        }
    }
}
