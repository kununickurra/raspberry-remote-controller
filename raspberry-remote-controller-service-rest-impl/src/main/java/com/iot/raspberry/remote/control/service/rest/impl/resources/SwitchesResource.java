package com.iot.raspberry.remote.control.service.rest.impl.resources;

import java.util.List;

/**
 * Resource representing a list of @link{SwitchResource}
 */
public class SwitchesResource extends AbstractResourceSupport<List<SwitchResource>> {
    public SwitchesResource(List<SwitchResource> content) {
        super(content);
    }
}
