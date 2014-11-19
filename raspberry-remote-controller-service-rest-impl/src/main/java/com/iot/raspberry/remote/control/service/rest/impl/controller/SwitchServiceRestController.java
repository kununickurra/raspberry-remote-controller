package com.iot.raspberry.remote.control.service.rest.impl.controller;

import com.iot.raspberry.remote.control.domain.PowerSwitch;
import com.iot.raspberry.remote.control.domain.SwitchState;
import com.iot.raspberry.remote.control.service.rest.impl.dto.SwitchDTO;
import com.iot.raspberry.remote.control.service.rest.impl.dto.SwitchStateDTO;
import com.iot.raspberry.remote.control.service.spec.exception.SwitchNotFoundException;
import com.iot.raspberry.remote.control.service.rest.impl.resources.SwitchResource;
import com.iot.raspberry.remote.control.service.rest.impl.resources.SwitchesResource;
import com.iot.raspberry.remote.control.service.spec.PowerSwitchBoxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * PowerSwitch Service REST Service implementation
 *
 * This services contains Hypermedia links to change the state of the switch to the next state.
 * USe PUT on turnOnSwitch/turnOffSwitch links to change the sate
 *
 * For Error handling, the {@link RestErrorHandler} ControllerAdvice will handle {@link  SwitchNotFoundException) and
 * return an error code and status.
 */

@RestController
@RequestMapping("/rest/powerswitch")
public class SwitchServiceRestController {

    public static final String TURN_ON_SWITCH_LINK = "turnOnSwitch";
    public static final String TURN_OFF_SWITCH_LINK = "turnOffSwitch";
    public static final String SWITCH_ID_PATH_VARIABLE = "switchId";

    private static Logger LOG = LoggerFactory.getLogger(SwitchServiceRestController.class);

    @Autowired
    private PowerSwitchBoxService switchService;

    /**
     * @return a HttpEntity containing a list of {@link SwitchesResource<SwitchDTO>} holding all configured switches.
     * @throws SwitchNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<SwitchesResource> getAllSwitches() throws SwitchNotFoundException {
        LOG.info("Getting all powerSwitches...");
        List<SwitchResource> returnedList = new ArrayList<SwitchResource>();
        Collection<PowerSwitch> powerSwitches = switchService.getConfiguredPowerSwitches();
        for (PowerSwitch item : powerSwitches) {
            SwitchResource resource = buildSwitchResource(item.getId(), item);
            returnedList.add(resource);
        }
        SwitchesResource resource = new SwitchesResource(returnedList);
        buildSwitchesResource(resource);
        return new ResponseEntity<SwitchesResource>(resource, HttpStatus.OK);
    }

    /**
     * @param switchId
     * @return a HttpEntity containing a {@link SwitchResource<SwitchDTO>} that corresponds to the PowerSwitch that matches the id provided
     * @throws SwitchNotFoundException in case no configured power switch matches the id provided.
     */
    @RequestMapping(value = "/{switchId}", method = RequestMethod.GET)
    public HttpEntity<SwitchResource> getSwitch(@PathVariable(SWITCH_ID_PATH_VARIABLE) Integer switchId) throws SwitchNotFoundException {
        LOG.info("Getting switch {}...", switchId);
        PowerSwitch powerSwitch = switchService.getPowerSwitch(switchId);
        if (powerSwitch == null) {
            throw new SwitchNotFoundException(switchId);
        }
        SwitchResource resource = buildSwitchResource(switchId, powerSwitch);
        return new ResponseEntity<SwitchResource>(resource, HttpStatus.OK);
    }

    /**
     * Turns on the Power Switch that matches the is provided.
     * @param switchId
     * @return a HttpEntity without answer content.
     * @throws SwitchNotFoundException in case no configured power switches matches the id provided.
     */
    @RequestMapping(value = "/{switchId}/turnon", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity<SwitchResource> turnOn (@PathVariable(SWITCH_ID_PATH_VARIABLE) Integer switchId) throws SwitchNotFoundException {
        LOG.info("Turning on switch {}", switchId);
        switchService.setSwitchState(switchId, SwitchState.ON);
        return new ResponseEntity<SwitchResource>(HttpStatus.NO_CONTENT);
    }

    /**
     * Turns Off the Power Switch that matches the is provided.
     * @param switchId
     * @return a HttpEntity without answer content.
     * @throws SwitchNotFoundException in case no configured power switches matches the id provided.
     */
    @RequestMapping(value = "/{switchId}/turnoff", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity<SwitchResource> turnOff(@PathVariable(SWITCH_ID_PATH_VARIABLE) Integer switchId) throws SwitchNotFoundException {
        LOG.info("Turning off switch {}", switchId);
        switchService.setSwitchState(switchId, SwitchState.OFF);
        return new ResponseEntity<SwitchResource>(HttpStatus.NO_CONTENT);
    }

    private SwitchResource buildSwitchResource(Integer switchId, PowerSwitch powerSwitch) throws SwitchNotFoundException {
        SwitchResource resource = new SwitchResource(buildSwitchDTO(powerSwitch));
        addSwitchHypermediaLinks(powerSwitch, resource);
        return resource;
    }

    private void buildSwitchesResource(SwitchesResource resource) throws SwitchNotFoundException {
        resource.add(linkTo(methodOn(SwitchServiceRestController.class).getAllSwitches()).withSelfRel());
    }

    private SwitchDTO buildSwitchDTO(PowerSwitch powerSwitch) {
        SwitchDTO dto = new SwitchDTO();
        dto.setId(powerSwitch.getId());
        dto.setName(powerSwitch.getName());
        dto.setSwitchState(SwitchStateDTO.valueOf(powerSwitch.getState().name()));
        return dto;
    }

    private void addSwitchHypermediaLinks(PowerSwitch powerSwitch, SwitchResource resource) throws SwitchNotFoundException {
        // Add self link
        resource.add(linkTo(methodOn(SwitchServiceRestController.class).getSwitch(powerSwitch.getId())).withSelfRel());
        // Only return possible operation given the current state of the resource
        if (powerSwitch.getState() == SwitchState.OFF) {
            resource.add(linkTo(methodOn(SwitchServiceRestController.class).turnOn(powerSwitch.getId())).withRel(TURN_ON_SWITCH_LINK));
        } else {
            resource.add(linkTo(methodOn(SwitchServiceRestController.class).turnOff(powerSwitch.getId())).withRel(TURN_OFF_SWITCH_LINK));
        }
    }
}
