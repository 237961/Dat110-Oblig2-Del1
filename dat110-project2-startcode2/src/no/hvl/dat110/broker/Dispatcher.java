package no.hvl.dat110.broker;

import java.util.Set;
import java.util.Collection;

import no.hvl.dat110.common.TODO;
import no.hvl.dat110.common.Logger;
import no.hvl.dat110.common.Stopable;
import no.hvl.dat110.messages.*;
import no.hvl.dat110.messagetransport.Connection;

public class Dispatcher extends Stopable {

	private Storage storage;

	public Dispatcher(Storage storage) {
		super("Dispatcher");
		this.storage = storage;

	}

	@Override
	public void doProcess() {

		Collection<ClientSession> clients = storage.getSessions();

		Logger.lg(".");
		for (ClientSession client : clients) {

			Message msg = null;

			if (client.hasData()) {
				msg = client.receive();
			}

			// a message was received
			if (msg != null) {
				dispatch(client, msg);
			}
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void dispatch(ClientSession client, Message msg) {

		MessageType type = msg.getType();

		// invoke the appropriate handler method
		switch (type) {

		case DISCONNECT:
			onDisconnect((DisconnectMsg) msg);
			break;

		case CREATETOPIC:
			onCreateTopic((CreateTopicMsg) msg);
			break;

		case DELETETOPIC:
			onDeleteTopic((DeleteTopicMsg) msg);
			break;

		case SUBSCRIBE:
			onSubscribe((SubscribeMsg) msg);
			break;

		case UNSUBSCRIBE:
			onUnsubscribe((UnsubscribeMsg) msg);
			break;

		case PUBLISH:
			onPublish((PublishMsg) msg);
			break;

		default:
			Logger.log("broker dispatch - unhandled message type");
			break;

		}
	}

	// called from Broker after having established the underlying connection
	public void onConnect(ConnectMsg msg, Connection connection) {

		String user = msg.getUser();
		
		//add user to message hashmap.
		System.out.println("userConnect: " + user);
		
		if(!storage.finnesBrukerMsg(user)) {
			storage.addMessageUser(user);
		}

		System.out.println("Message size: " + storage.getMessageSize());

		Logger.log("onConnect:" + msg.toString());
		
		Set<PublishMsg> msgs = storage.getMessages(user);
		storage.addClientSession(user, connection);
		
		ClientSession session = storage.getSession(user);
		
		System.out.println("Queued messages: " + msgs.size());
		
		if(msgs.size() > 0) {
			for(PublishMsg m : msgs) {
				System.out.println(m.toString());
				session.send(m);
			}
			storage.removeMessages(user);
		}
		

	}

	// called by dispatch upon receiving a disconnect message
	public void onDisconnect(DisconnectMsg msg) {

		String user = msg.getUser();

		Logger.log("onDisconnect:" + msg.toString());

		storage.removeClientSession(user);

	}

	public void onCreateTopic(CreateTopicMsg msg) {
		
		String topic = msg.getTopic();
		
		storage.createTopic(topic);

		Logger.log("onCreateTopic:" + msg.toString());

		// TODO: create the topic in the broker storage
		// the topic is contained in the create topic message

	}

	public void onDeleteTopic(DeleteTopicMsg msg) {
		
		String topic = msg.getTopic();
		
		storage.deleteTopic(topic);

		Logger.log("onDeleteTopic:" + msg.toString());

		// TODO: delete the topic from the broker storage
		// the topic is contained in the delete topic message
	}

	public void onSubscribe(SubscribeMsg msg) {

		String topic = msg.getTopic();
		String user = msg.getUser();
		
		storage.addSubscriber(user, topic);
		
		Logger.log("onSubscribe:" + msg.toString());

		// TODO: subscribe user to the topic
		// user and topic is contained in the subscribe message
	}

	public void onUnsubscribe(UnsubscribeMsg msg) {
		
		String topic = msg.getTopic();
		String user = msg.getUser();
		
		storage.removeSubscriber(user, topic);

		Logger.log("onUnsubscribe:" + msg.toString());

		// TODO: unsubscribe user to the topic
		// user and topic is contained in the unsubscribe message
	}

	public void onPublish(PublishMsg msg) {
		
		String topic = msg.getTopic();
		
		Set<String> clients = storage.getSubscribers(topic);
		
		
		for(String user : clients) { 
			ClientSession session = storage.getSession(user);
			
			if(session != null) {
				session.send(msg);
			} else {
				storage.addMessage(user, msg);
			}
			
			
		}
		 
		Logger.log("onPublish:" + msg.toString());

		// TODO: publish the message to clients subscribed to the topic
		// topic and message is contained in the subscribe message
		// messages must be sent used the corresponding client session objects
	}
	
}
