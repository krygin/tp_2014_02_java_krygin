/**
 * Created by Ivan on 12.02.14.
 */

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import sun.plugin2.message.Message;

import javax.servlet.Servlet;

public class Main {
    public static void main(String[] args) throws Exception {
        MessageSystem messageSystem = new MessageSystem();
        Frontend frontend = new Frontend(messageSystem);
        AccountService accountService = new AccountService(messageSystem);

        (new Thread(frontend)).start();
        (new Thread(accountService)).start();

        Server server = new Server(8080);
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(frontend), "/*");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, servletContextHandler});
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}