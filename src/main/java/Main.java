/**
 * Created by Ivan on 12.02.14 in 19:43.
 */

import message_system.MessageSystem;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import resource_system.ResourceFactory;
import resource_system.VFS;
import resource_system.resources.ServerConfiguration;
import services.account_service.AccountService;
import services.frontend.Frontend;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        final VFS vfs = new VFS("src/main/resources/");
        final ServerConfiguration configuration = (ServerConfiguration) ResourceFactory.getInstance().getResource(vfs.getPath("server.cfg.xml"));
        final MessageSystem messageSystem = new MessageSystem();
        Frontend frontend = new Frontend(messageSystem, vfs);

        final List<AccountService> accountServices = new ArrayList<AccountService>() {{
            for (int i = 0; i < configuration.NUMBER_OF_ACCOUNT_SERVICES(); i++)
                add(new AccountService(messageSystem, vfs));
        }};
        (new Thread(frontend)).start();
        for (AccountService accountService : accountServices)
            (new Thread(accountService)).start();

        Server server = new Server(configuration.PORT());
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