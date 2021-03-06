package com.github.zulkar.transaction.web;

import com.github.zulkar.transaction.model.User;
import com.github.zulkar.transaction.processing.ProcessingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.zulkar.transaction.web.Utils.validateUserNotNull;

@Path("/users")
public class UserService {
    private static final Logger LOG = LogManager.getLogger(UserService.class);
    private final ProcessingService processingService;

    @Inject
    public UserService(@NotNull ProcessingService processingService) {
        this.processingService = processingService;
    }

    @POST
    @Path("{user}/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@PathParam("user") @Nullable String username) {
        LOG.debug("/users/create: user{}", username);
        validateUserNotNull(username);
        processingService.createAccount(new User(username));
        return Response.status(Response.Status.OK).entity(StatusMessage.OK).build();
    }


    @GET
    @Path("{user}/balance")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBalance(@PathParam("user") @Nullable String username) {
        LOG.debug("/users/getBalance: user{}", username);
        validateUserNotNull(username);
        BigDecimal balance = processingService.getBalance(new User(username));
        return Response.status(Response.Status.OK).entity(new BalanceEntity(username, balance)).build();
    }

    private static class BalanceEntity {
        private final String user;
        private final BigDecimal balance;

        private BalanceEntity(String user, BigDecimal balance) {
            this.user = user;
            this.balance = balance;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public String getUser() {
            return user;
        }
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        LOG.debug("/users/all");
        return Response.status(Response.Status.OK)
                .entity(processingService.getAllUsersWithBalance()
                        .entrySet().stream()
                        .collect(Collectors.toMap(it -> it.getKey().getName(), Map.Entry::getValue)))
                .build();
    }


}
