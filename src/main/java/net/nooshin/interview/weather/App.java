package net.nooshin.interview.weather;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * Hello world!
 *
 */
public class App 
{
	public static Properties createProducerConfig() throws UnknownHostException {
		Properties conf = new Properties();
		conf.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		conf.put(ProducerConfig.ACKS_CONFIG, "all");
		conf.put(ProducerConfig.CLIENT_ID_CONFIG, InetAddress.getLocalHost().getHostName());
		conf.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		conf.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DoubleSerializer.class.getName());
		System.out.println(conf);
		return conf;
	}
	
    public static void main( String[] args )
    {
    	Properties producerConf;
		try {
			producerConf = createProducerConfig();
			Ingestion in = new Ingestion(producerConf);
			Thread t = new Thread(in);
			t.start();
//			in.run();
			
			t.join();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
       
    }
}
