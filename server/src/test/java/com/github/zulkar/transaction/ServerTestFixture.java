package com.github.zulkar.transaction;

import org.junit.jupiter.api.extension.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;

public class ServerTestFixture implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ServerUri {
    }

    private TransactionServer server;
    private URI uri;

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        if (server != null) {
            server.destroy();
        }
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        String passedParam = System.getProperty("TRANSACTION_SERVER_URI");
        if (passedParam == null) {
            server = new TransactionServer();
            uri = server.getUri();
        } else {
            uri = new URI("http://" + passedParam);
        }

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (parameterContext.getParameter().getType() == URI.class)
                && parameterContext.isAnnotated(ServerUri.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return uri;
    }
}
