package org.cs27x.di;

import java.net.UnknownHostException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class RestServer {

	private static final String CONTEXT_PATH = "/";

	private static final String DEFAULT_PORT = "8080";

	private static final String WAR_PATH = "war";

	private static final String WEB_XML_PATH = "war/WEB-INF/web.xml";

	private static final String PORT = "port";
	
	private Server server_;

	public boolean start() {
		try {
			int port = Integer.parseInt(System.getProperty(PORT, DEFAULT_PORT));
			WebAppContext context = new WebAppContext();
			context.setDescriptor(WEB_XML_PATH);
			context.setResourceBase(WAR_PATH);
			context.setContextPath(CONTEXT_PATH);

			server_ = new Server(port);
			server_.setHandler(context);
			server_.start();
			System.out.println("[Server started]\n");

		} catch (UnknownHostException e) {
			System.out.println("[Error] Unable to start web server");
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			System.out.println("[Error] Unable to start web server");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public void stop(int delay) {
		try {
			server_.stop();
			System.out.println("[Server halted]");
		} catch (Exception e) {
			System.out.println("[Error] Unable to stop the server");
		}
	}

	public static void main(String[] args) {
		RestServer server = new RestServer();

		if (!server.start())
			return;
	}
}
