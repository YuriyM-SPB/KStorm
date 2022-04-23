import java.util.Map;
import java.io.*;
import java.util.HashMap;

import org.apache.storm.tuple.Tuple;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.task.TopologyContext;

public class CountBolt implements IRichBolt{
    Map<String, Integer> counters;
    private OutputCollector collector;
    
    private String logFile = "storm_output.txt";
    private Map<String, Integer> counts = new HashMap<String, Integer>();

    @Override
    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.counters = new HashMap<String, Integer>();
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String str = input.getString(0);
        if(!counters.containsKey(str)){
            counters.put(str, 1);
        }else {
            Integer c = counters.get(str) +1;
            counters.put(str, c);
        }

        collector.ack(input);
        
        if(str.contains("stopcount")) {
			FileWriter writer;
			try {
				writer = new FileWriter("/tmp/stopper.txt", false);
				writer.write(str);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

        }
        
    }

    @Override
    public void cleanup() {
    	try {
			FileWriter writer = new FileWriter(logFile, false);
			BufferedWriter bw = new BufferedWriter(writer);
			writer.write("WORD COUNT RESULTS: " + System.getProperty( "line.separator" ));
			bw.newLine();
	    	
	        for(Map.Entry<String, Integer> entry:counters.entrySet()){
	        	writer.write(entry.getKey()+" : " + entry.getValue() + System.getProperty( "line.separator" ));
	        	bw.newLine();
	        }
	        writer.close();
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
    	

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
