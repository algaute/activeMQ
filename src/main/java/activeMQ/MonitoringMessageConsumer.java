package activeMQ;

public class MonitoringMessageConsumer implements Runnable {

	private Thread activeMQ;
	
	public MonitoringMessageConsumer(Thread activeMQ) {
		this.activeMQ=activeMQ;
	}
	
	@Override
	public void run() {
		try
		{
			while (true) {
				if (activeMQ.isAlive())
				{
					System.out.println("Thread activeMQMessageConsumer is alive");
				} 
				else 
				{
					System.out.println("Thread activeMQMessageConsumer is dead");
				}
				
	        	Thread.sleep(5000);
	
			}
		}
		catch (InterruptedException e) {
			System.out.println("MonitoringMessageConsumer Exception : "+e.getMessage());
			Thread t = Thread.currentThread();
		    t.getUncaughtExceptionHandler().uncaughtException(t, e);
		}
	}
}