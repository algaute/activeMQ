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
		
		//BrokerService broker=null;
    	Connection connection=null;
    	Session session=null;
		try
		{
			try
			{
				// configure the broker
				// --------------------
				// no console if broker embeed, see with hawt.io http://hawt.io/ 
				// or follow http://activemq.2283324.n4.nabble.com/embedded-broker-with-admin-web-console-td2366789.html
				//
				// best way for me is to download activemq and start the broker as a service 
				// --------------------
			    //broker = new BrokerService();
				//broker.addConnector(messageBrokerUrl);
				//broker.start();	
				
		    	// Configure the connection
		    	ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messageBrokerUrl);  
		    	connection = connectionFactory.createConnection();			
				session = connection.createSession(transacted, ackMode);
	            Queue queue = session.createQueue(messageQueueName);
	    	            
		  	   	// Start 5 consumers
	            for (int i = 0; i < 5; i++) {
	                MessageConsumer consumer = session.createConsumer(queue);
	                consumer.setMessageListener(new AppMessageListener("Consumer " + i));
	            }
	            connection.start();
	            
	            // wait indefinitely
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
    	        //if (broker != null) {
    	        //    broker.stop();
    	        //}
    		}
		}
		catch (JMSException e)
		{
			System.out.println("AppMessageConsumer JMSException : "+e.getMessage());

			logger.error("AppMessageConsumer JMSException : "+e.getMessage(),e);
			Thread t = Thread.currentThread();
		    t.getUncaughtExceptionHandler().uncaughtException(t, e);
		}
		catch (Exception e) 
		{
			System.out.println("AppMessageConsumer Exception : "+e.getMessage());

			logger.error("AppMessageConsumer Exception : "+e.getMessage(),e);
		    Thread t = Thread.currentThread();
		    t.getUncaughtExceptionHandler().uncaughtException(t, e);
		}
    }


}