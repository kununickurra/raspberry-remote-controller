package com.iot.raspberry.ctrl.service.rest.impl.resources;

import com.iot.raspberry.ctrl.service.rest.impl.dto.SwitchDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Resource representing a list of @link{SwitchDTO}
 */
public class SwitchResource extends AbstractResourceSupport<SwitchDTO> {
    @JsonCreator
    public SwitchResource(@JsonProperty("content") SwitchDTO content) {
        super(content);
    }
}
