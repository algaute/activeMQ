package activeMQ;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppMessageConsumer implements Runnable {

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

    public AppMessageConsumer() {
    }
    
	@Override
	public void run()
	{
		this.connectionFactory = new ActiveMQConnectionFactory(messageBrokerUrl);  
    	Connection connection=null;
    	Session session=null;
		try
		{
			try
			{
				connection = this.connectionFactory.createConnection();			
				session = connection.createSession(transacted, ackMode);
	            Queue queue = session.createQueue(messageQueueName);
	    	            
		  	   	// Consumer
	            for (int i = 0; i < 4; i++) {
	                MessageConsumer consumer = session.createConsumer(queue);
	                consumer.setMessageListener(new AppMessageListener("Consumer " + i));
	            }
	            connection.start();
	            
	            while (true) {
	            	Thread.sleep(5000);
	            }
			}
			finally 
    		{
    			if (session!=null) {
    				session.close();
    			}
    	        if (connection != null) {
    	            connection.close();
    	        }
    	        //broker.stop();
    		}
		}
		catch (JMSException e)
		{
			logger.error("Consumer's not started due to error : "+e.getMessage(),e);
			Thread t = Thread.currentThread();
		    t.getUncaughtExceptionHandler().uncaughtException(t, e);
		}
		catch (Exception e) 
		{
			logger.error("Benera exception : "+e.getMessage(),e);
		    Thread t = Thread.currentThread();
		    t.getUncaughtExceptionHandler().uncaughtException(t, e);
		}
    }


}