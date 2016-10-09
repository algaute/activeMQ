package activeMQ;

import java.lang.Thread.State;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationPath("/")
public class Bootstrap extends javax.ws.rs.core.Application {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public Bootstrap(@Context ServletContext servletContext) throws Exception {
		logger.info("----------------------------");
		logger.info("Initializing Web Services...");
		logger.info("----------------------------");
		
		Thread activeMQ = new Thread(new AppMessageConsumer());
		activeMQ.setName("activeMQMessageConsumer");
		activeMQ.start();
		
		logger.info("-----------------------------");
		logger.info("Web Services ready!");
		logger.info("-----------------------------");

	}

}
