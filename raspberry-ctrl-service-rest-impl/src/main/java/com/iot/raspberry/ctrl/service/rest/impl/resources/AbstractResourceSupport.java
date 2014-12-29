package com.iot.raspberry.ctrl.service.rest.impl.resources;

import org.springframework.hateoas.ResourceSupport;

/**
 * Abstract superclass for resources returned by the REST services.
 */
public abstract class AbstractResourceSupport<T> extends ResourceSupport {

    private T content;

    protected AbstractResourceSupport(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }
}
