package no.hvl.dat110.messages;

public class CreateTopicMsg extends Message {
	
	// message sent from client to create topic on the broker
	private String topic;
	
	// TODO: 
	// Implement object variables - a topic is required
	
	public CreateTopicMsg(String topic) {
		super();
		this.topic = topic;
	}
	
	public CreateTopicMsg(String user, String topic) {
		super(MessageType.CREATETOPIC, user);
		this.topic = topic;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topicName) {
		this.topic = topicName;
	}

	@Override
	public String toString() {
		return "CreateTopicMsg [topic=" + topic + "]";
	}
	
	
	
	// Constructor, get/set-methods, and toString method
    // as described in the project text	
}
