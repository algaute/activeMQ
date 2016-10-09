package activeMQ;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppMessageListener implements MessageListener {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private String consumerName;
    public AppMessageListener(String consumerName) 
    {
        this.consumerName = consumerName;
    }
	
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
		
		// We acknowledge the message, so we don't receive it again:
		message.acknowledge();

		System.out.println(this.consumerName+" received this message "+message.getJMSCorrelationID());
		logger.info(this.consumerName+" received this message "+message.getJMSCorrelationID());
	}
	
	protected void notifyError(Exception e)
	{
		// notify error by email or other stuff
	}

}
