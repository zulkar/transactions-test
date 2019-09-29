package com.github.zulkar.transaction.web;

import com.github.zulkar.transaction.model.User;
import com.github.zulkar.transaction.processing.ProcessingService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static com.github.zulkar.transaction.web.Utils.validateAmountNotNull;
import static com.github.zulkar.transaction.web.Utils.validateUserNotNull;

@Path("/operations")
public class OperationsService {

    private final ProcessingService processingService;

    @Inject
    public OperationsService(@NotNull ProcessingService processingService) {
        this.processingService = processingService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transfer")
    public Response transfer(@QueryParam("from") @Nullable String from, @QueryParam("to") @Nullable String to, @QueryParam("amount") @Nullable BigDecimal amount) {
        validateUserNotNull(from);
        validateUserNotNull(to);
        validateAmountNotNull(amount);
        processingService.transfer(new User(from), new User(to), amount);
        return Response.status(Response.Status.OK).entity(StatusMessage.OK).build();
    }

    @POST
    @Path("replenish/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response replenish(@PathParam("user") @Nullable String username, @QueryParam("amount") @Nullable BigDecimal amount) {
        validateUserNotNull(username);
        validateAmountNotNull(amount);
        BigDecimal balance = processingService.replenish(new User(username), amount);
        return Response.status(Response.Status.OK).entity(StatusMessage.OK).build();
    }
}
