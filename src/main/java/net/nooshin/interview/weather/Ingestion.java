package net.nooshin.interview.weather;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.openweathermap.api.DataWeatherClient;
import org.openweathermap.api.UrlConnectionDataWeatherClient;
import org.openweathermap.api.model.currentweather.CurrentWeather;
import org.openweathermap.api.query.*;
import org.openweathermap.api.query.currentweather.CurrentWeatherOneLocationQuery;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Ingestion implements Runnable{

	final KafkaProducer<String, Double> producer;
	final static String API_KEY = "ec91d9795e336642d7c052e8064ffe18";
	public Ingestion(Properties conf) {
		
		producer = new KafkaProducer<String, Double>(conf);
	}
	
	public double getTemperature(String city) throws IOException {
		DataWeatherClient client = new UrlConnectionDataWeatherClient(API_KEY);
        CurrentWeatherOneLocationQuery currentWeatherOneLocationQuery = QueryBuilderPicker.pick()
                .currentWeather()                   // get current weather
                .oneLocation()                      // for one location
                .byCityName(city)              // for Kharkiv city
                .countryCode("CH")                  // in Ukraine
                .type(Type.ACCURATE)                // with Accurate search
                .language(Language.ENGLISH)         // in English language
                .responseFormat(ResponseFormat.JSON)// with JSON response format
                .unitFormat(UnitFormat.METRIC)      // in metric units
                .build();
        CurrentWeather currentWeather = client.getCurrentWeather(currentWeatherOneLocationQuery);
        
        return currentWeather.getMainParameters().getTemperature();
	
	}

	public void run() {
		ArrayList<String> cities = new ArrayList<String>(
				Arrays.asList("bern", "lausanne", "zurich"));
		
		while(true) {
			for (String city: cities) {
				try {
					double t = getTemperature(city);
					System.out.println("city = " + city + " temp = " + t);
					ProducerRecord<String, Double> pr = new 
							ProducerRecord<String, Double>("temperature",city, t);
					System.out.println(pr);
					producer.send(pr, (metadata, e) -> {
						if (e != null) { e.printStackTrace(); }});
					producer.flush();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void shutDown() {
		
	}

}
