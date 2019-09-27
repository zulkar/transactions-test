package com.github.zulkar.transaction.web;

import com.github.zulkar.transaction.model.User;
import com.github.zulkar.transaction.processing.ProcessingService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static com.github.zulkar.transaction.web.Utils.validateAmountNotNull;
import static com.github.zulkar.transaction.web.Utils.validateUserNotNull;

@Path("/operations")
public class TransferService {

    private final ProcessingService processingService;

    @Inject
    public TransferService(@NotNull ProcessingService processingService) {
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
        return Response.status(Response.Status.OK).build();
    }
}
