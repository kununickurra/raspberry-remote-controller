package com.iot.raspberry.remote.control.service.support;

import com.iot.raspberry.remote.control.domain.SwitchState;
import com.pi4j.io.gpio.PinState;
import org.springframework.stereotype.Component;

@Component
public class SwitchStateToPinStateConverter {

    public PinState convert(SwitchState switchState) {
        return (switchState == SwitchState.OFF) ? PinState.HIGH : PinState.LOW;
    }
}