package com.iot.raspberry.remote.control.webapp.controller;

import com.iot.raspberry.remote.control.service.spec.PowerSwitchBoxService;
import com.iot.raspberry.remote.control.domain.SwitchState;
import com.iot.raspberry.remote.control.service.spec.exception.SwitchNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by tilliern on 11/11/2014.
 */
@Controller
public class PowerSwitchStatusWebController {

    //TODO: Test this controller
    private static final Logger LOG = LoggerFactory.getLogger(PowerSwitchStatusWebController.class);

    @Autowired
    private PowerSwitchBoxService powerSwitchBoxService;

    @RequestMapping(value = "/switches", method = RequestMethod.GET)
    public ModelAndView status() {
        ModelAndView modelAndView = new ModelAndView("switches");
        modelAndView.addObject("switchList", powerSwitchBoxService.getConfiguredPowerSwitches());
        return modelAndView;
    }

    @RequestMapping("/switches/setState")
    public ModelAndView setState(@RequestParam("switchId") int switchId, @RequestParam("state") String state) throws SwitchNotFoundException {
        LOG.info("Setting state of Switch {} to {}...", switchId, state);
        powerSwitchBoxService.setSwitchState(switchId, SwitchState.valueOf(state));
        return status();
    }
}
