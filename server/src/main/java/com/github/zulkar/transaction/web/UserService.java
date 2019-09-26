package com.github.zulkar.transaction.web;

import com.github.zulkar.transaction.model.User;
import com.github.zulkar.transaction.processing.ProcessingService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static com.github.zulkar.transaction.web.Utils.validateAmountNotNull;
import static com.github.zulkar.transaction.web.Utils.validateUserNotNull;

@Path("/users")
public class UserService {
    private final ProcessingService processingService;

    public UserService(@NotNull ProcessingService processingService) {
        this.processingService = processingService;
    }

    @POST
    @Path("{$user}/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@PathParam("user") @Nullable String username) {
        validateUserNotNull(username);
        processingService.createAccount(new User(username));
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("{$user}/replenish")
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@PathParam("user") @Nullable String username, @QueryParam("amount") @Nullable BigDecimal amount) {
        validateUserNotNull(username);
        validateAmountNotNull(amount);
        BigDecimal balance = processingService.replenish(new User(username), amount);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("${user}/balance")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBalance(@PathParam("user") @Nullable String username) {
        validateUserNotNull(username);
        BigDecimal balance = processingService.getBalance(new User(username));
        return Response.status(Response.Status.OK).build();
    }


}
