package activeMQ;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageConsumer implements MessageListener {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void onMessage(Message message) {
		try
		{
			processMessage(message);
		}
		catch (Exception e)
		{
			notifyError(e);
			throw new RuntimeException(e);
		}		
	}
	
	public void processMessage(Message message) throws Exception, JMSException
	{
		MessageProtocol messageProtocol=new MessageProtocol();
		
		// get the message and process it
		messageProtocol.setProperty1(message.getStringProperty("property1"));
		messageProtocol.setProperty2(message.getStringProperty("property2"));
		messageProtocol.setProperty3(message.getStringProperty("property3"));
				
		//Service.insertIntoDatabase(messageProtocol);
		System.out.println(messageProtocol.toString());
		
		// We acknowledge the message, so we don't receive it again:
		message.acknowledge();

		logger.info("message "+message.getJMSCorrelationID()+" received");
	}
	
	protected void notifyError(Exception e)
	{
		// notify error by email or other stuff
	}

}
