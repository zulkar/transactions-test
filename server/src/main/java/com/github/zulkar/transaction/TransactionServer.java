package com.github.zulkar.transaction;

import com.github.zulkar.transaction.web.BusinessExceptionMapper;
import com.github.zulkar.transaction.web.OperationsService;
import com.github.zulkar.transaction.web.UserService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TransactionServer {

    private final Server server;

    public TransactionServer() throws Exception {
        this(0);
    }

    public TransactionServer(int port) throws Exception {
        server = new Server(port);
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);
        initJersey(context,
                UserService.class, OperationsService.class, BusinessExceptionMapper.class);

        server.start();
    }

    public URI getPort() {
        return server.getURI();
    }

    public void destroy() throws Exception {
        server.stop();
        server.destroy();

    }

    public void join() throws InterruptedException {
        server.join();
    }

    private ServletHolder initJersey(ServletContextHandler context,
                                     Class<?>... klasses) {
        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter("javax.ws.rs.Application", MyApplication.class.getCanonicalName());
        jerseyServlet.setInitParameter("jersey.config.server.exception.processResponseErrors", "TRUE");
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                Arrays.stream(klasses).map(Class::getCanonicalName).collect(Collectors.joining(",")));
        return jerseyServlet;
    }
}
