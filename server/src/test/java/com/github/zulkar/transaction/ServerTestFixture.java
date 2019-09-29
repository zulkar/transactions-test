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

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        server.destroy();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        server = new TransactionServer();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (parameterContext.getParameter().getType() == URI.class)
                && parameterContext.isAnnotated(ServerUri.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return server.getPort();
    }
}
