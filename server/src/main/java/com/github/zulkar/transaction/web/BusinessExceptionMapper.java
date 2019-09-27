package com.github.zulkar.transaction.web;

import com.github.zulkar.transaction.errors.BusinessException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {
    @Override
    public Response toResponse(BusinessException e) {
        return Response.status(422).entity(new ErrorMessage(e))
                .type(MediaType.APPLICATION_JSON).
                        build();
    }

    public static class ErrorMessage {
        private final int status;
        private final String message;

        public ErrorMessage(BusinessException e) {
            this.status = e.getCode();
            this.message = e.getMessage();
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
