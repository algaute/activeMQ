package activeMQ;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

@Path("/message")
public class receiverResource {

	@GET
	@Path("/receive")
	public void receive(@Context HttpServletRequest httpReq, @QueryParam("p1") String p1, @QueryParam("p2") String p2, @QueryParam("p3") String p3) {
		try {
			MessageReceiver messageReceiver=new MessageReceiver();
			MessageProtocol messageProtocol = new MessageProtocol();
			messageProtocol.setProperty1(p1);
			messageProtocol.setProperty2(p2);
			messageProtocol.setProperty3(p3);
			messageReceiver.enqueueMessage(messageProtocol);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
