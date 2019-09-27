package com.github.zulkar.transaction;

import com.github.zulkar.transaction.web.TransferService;
import com.github.zulkar.transaction.web.UserService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args)  {
        int port = parsePort(args);
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);
        initJersey(context,
                UserService.class, TransferService.class);

        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            System.exit(1);
        } finally {
            server.destroy();
        }
    }

    private static ServletHolder initJersey(ServletContextHandler context,
                                            Class<?>... klasses) {
        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter("javax.ws.rs.Application", MyApplication.class.getCanonicalName());
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                Arrays.stream(klasses).map(Class::getCanonicalName).collect(Collectors.joining(",")));
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                Arrays.stream(klasses).map(Class::getCanonicalName).collect(Collectors.joining(",")));
        return jerseyServlet;
    }

    private static int parsePort(String[] args) {
        return 8090;
    }
}
