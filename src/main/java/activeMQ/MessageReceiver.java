package activeMQ;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.Random;

public class MessageReceiver {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	private ActiveMQConnectionFactory connectionFactory;
	
	private static String messageBrokerUrl;
	private static String messageQueueName;
    private static int ackMode;
    private boolean transacted = false;
 
    static {
        messageBrokerUrl = "tcp://localhost:61616";
        messageQueueName = "devices.teltonika";
        ackMode = Session.AUTO_ACKNOWLEDGE;
    }
    
    public MessageReceiver() 
    {
    	this.connectionFactory = new ActiveMQConnectionFactory(messageBrokerUrl);  
    }
    
	public void enqueueMessage(MessageProtocol messageProtocol) throws Exception {
		
		try
		{
			Connection connection = this.connectionFactory.createConnection();
			connection.start();			
			
			// Starts the producer...
			Session session = connection.createSession(transacted, ackMode);
            Destination adminQueue = session.createQueue(messageQueueName);
            
            //Setup a message producer to send message to the queue the server is consuming from
            MessageProducer producer = session.createProducer(adminQueue);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			
			// ... and produces a message.
			ObjectMessage message = session.createObjectMessage();
			
			//Set a correlation ID so when you get a response you know which sent message the response is for
            //If there is never more than one outstanding message to the server then the
            //same correlation ID can be used for all the messages...if there is more than one outstanding
            //message to the server you would presumably want to associate the correlation ID with this
            //message somehow...a Map works good
            String correlationId = this.createRandomString();
            message.setJMSCorrelationID(correlationId);
			
            message.setStringProperty("property1",  messageProtocol.getProperty1());
            message.setStringProperty("property2", messageProtocol.getProperty2());
            message.setStringProperty("property3", messageProtocol.getProperty3());
			
			producer.send(message);

			logger.info("Sent message to "+messageQueueName+" with correlationId "+correlationId+" and timestamp "+message.getJMSTimestamp());
			
			System.out.println("Message sent to "+messageQueueName+" in activeMQ");
			
			session.close();
			connection.close();
		}
		catch (JMSException e)
		{
			logger.error("Message not stored due to error "+e.getMessage(),e);
			throw new Exception("Message not stored due to error "+e.getMessage(),e);
		}
	}	

	private String createRandomString() {
	    Random random = new Random(System.currentTimeMillis());
	    long randomLong = random.nextLong();
	    return Long.toHexString(randomLong);
	}

}