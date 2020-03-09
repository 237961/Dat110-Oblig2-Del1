package no.hvl.dat110.iotsystem;

import no.hvl.dat110.client.Client;

public class DisplayDevice {
	
	private static final int COUNT = 10;
		
	public static void main (String[] args) {
		
		System.out.println("Display starting ...");
		
		// TODO - START
				
		// create a client object and use it to
		Client client = new Client("display", Common.BROKERHOST, Common.BROKERPORT);
		
		client.connect();
		client.createTopic(Common.TEMPTOPIC);
		
		client.subscribe(Common.TEMPTOPIC);
		
		for(int i = 0; i < COUNT; i++) {
		
			client.receive();
		
		}
		
		client.unsubscribe(Common.TEMPTOPIC);
		
		client.disconnect();
		
		// - connect to the broker
		// - create the temperature topic on the broker
		// - subscribe to the topic
		// - receive messages on the topic
		// - unsubscribe from the topic
		// - disconnect from the broker
		
		// TODO - END
		
		System.out.println("Display stopping ... ");
		
	}
}
