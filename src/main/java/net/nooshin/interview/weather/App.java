package net.nooshin.interview.weather;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Ingestion in = new Ingestion();
        try {
			System.out.println(in.getTemperature("bern"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
