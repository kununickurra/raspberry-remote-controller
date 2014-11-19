package com.iot.raspberry.remote.control.service.rest.impl.dto;

import org.springframework.hateoas.ResourceSupport;

/**
 * REST DTO used to return errors to the client.
 * A code and a description will be provided.
 */
public class ErrorDTO {

    private int code;
    private String message;

    public ErrorDTO(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {

        return code;
    }
}
