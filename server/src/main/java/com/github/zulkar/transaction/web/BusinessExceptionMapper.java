package com.github.zulkar.transaction.web;

import com.github.zulkar.transaction.errors.BusinessException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {
    @Override
    public Response toResponse(BusinessException e) {
        return Response.status(422).entity(new StatusMessage(e))
                .type(MediaType.APPLICATION_JSON).
                        build();
    }

}
