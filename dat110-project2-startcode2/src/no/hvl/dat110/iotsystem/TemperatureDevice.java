package no.hvl.dat110.iotsystem;

import java.util.concurrent.TimeUnit;

import no.hvl.dat110.client.Client;
import no.hvl.dat110.common.TODO;
import no.hvl.dat110.messages.PublishMsg;
import no.hvl.dat110.messagetransport.MessageConfig;

public class TemperatureDevice {

	private static final int COUNT = 10;

	public static void main(String[] args) {

		// simulated / virtual temperature sensor
		TemperatureSensor sn = new TemperatureSensor();

		// TODO - start

		// create a client object and use it to
		
		Client client = new Client("temperaturesensor", Common.BROKERHOST, Common.BROKERPORT);
		
		client.connect();
		
		for(int i = 0; i < COUNT; i++) {
			
			int temp = sn.read();
		
			client.publish(Common.TEMPTOPIC, String.valueOf(temp));
			
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		}
		
		client.disconnect();

		// - connect to the broker
		// - publish the temperature(s)
		// - disconnect from the broker

		// TODO - end

		System.out.println("Temperature device stopping ... ");

	}
}
