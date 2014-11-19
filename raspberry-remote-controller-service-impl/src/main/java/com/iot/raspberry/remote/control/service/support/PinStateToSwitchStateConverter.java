package com.iot.raspberry.remote.control.service.support;

import com.iot.raspberry.remote.control.domain.SwitchState;
import com.pi4j.io.gpio.PinState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PinStateToSwitchStateConverter {

    public SwitchState convert(PinState pinState) {
        return (pinState == PinState.HIGH) ? SwitchState.OFF : SwitchState.ON;
    }
}