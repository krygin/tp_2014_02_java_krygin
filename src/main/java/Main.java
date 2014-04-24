/**
 * Created by Ivan on 12.02.14 in 19:43.
 */

import constants.Constants;
import message_system.MessageSystem;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import services.account_service.AccountService;
import services.frontend.Frontend;

import java.util.ArrayList;
import java.util.List;

import static constants.DatabaseConfiguration.MYSQL_DATABASE_CONFIGURATION;

public class Main {

    public static void main(String[] args) throws Exception {
        final String databaseConfiguration = MYSQL_DATABASE_CONFIGURATION;


        final MessageSystem messageSystem = new MessageSystem();
        Frontend frontend = new Frontend(messageSystem);

        final List<AccountService> accountServices = new ArrayList<AccountService>() {{
            for (int i = 0; i < Constants.NUMBER_OF_ACCOUNT_SERVICES; i++)
                add(new AccountService(messageSystem, databaseConfiguration));
        }};
        (new Thread(frontend)).start();
        for (AccountService accountService : accountServices)
            (new Thread(accountService)).start();

        Server server = new Server(8080);
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(frontend), "/*");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, servletContextHandler});
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
