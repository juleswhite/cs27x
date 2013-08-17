/* 
**
** Copyright 2013, Jules White
**
** 
*/
package org.magnum.sodacloud.example.sms;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.magnum.soda.Soda;
import org.magnum.soda.server.wamp.JettyConfigurer;
import org.magnum.soda.server.wamp.ServerConfig;
import org.magnum.soda.server.wamp.ServerSoda;
import org.magnum.soda.server.wamp.ServerSodaLauncher;
import org.magnum.soda.server.wamp.ServerSodaListener;
import org.magnum.soda.server.wamp.WampServer;
import org.magnum.soda.server.wamp.WebsocketServletAdapter;
import org.magnum.soda.server.wamp.adapters.jetty.JettyServerHandler;

public class SMSBridgeServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerSodaLauncher launcher = new ServerSodaLauncher();
		
		ServerConfig conf = new ServerConfig();
		conf.setPath("/sms/");
		conf.setConfigurer(new JettyConfigurer() {
			
			@Override
			public void configure(Server s, WampServer wamp, JettyServerHandler hdlr) {
				WebsocketServletAdapter servlet = new WebsocketServletAdapter() {
					
					@Override
					protected void started(ServerSoda soda) {
						// TODO Auto-generated method stub
						
					}
				};
				servlet.setWampServer(wamp);
				servlet.setPort(8081);
				ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		        context.setContextPath("/");
		      
		        ResourceHandler resource_handler = new ResourceHandler();
		        resource_handler.setDirectoriesListed(true);
		        resource_handler.setWelcomeFiles(new String[]{ "index.html" });
		 
		        resource_handler.setResourceBase("./resources/view/");
		 
		        HandlerList handlers = new HandlerList();
		        handlers.setHandlers(new Handler[] { context, resource_handler, new DefaultHandler() });
		        s.setHandler(handlers);
		        
		       // s.setHandler(context);
		 
		        context.addServlet(new ServletHolder(servlet),"/sms/*");
			}
		});
		
		launcher.launch(conf, new ServerSodaListener() {
			
			@Override
			public void started(Soda arg0) {
				arg0.bind(new Runnable() {
					
					@Override
					public void run() {
						System.out.println("foo");
					}
				}, "foo");
			}
		});
	}

}
