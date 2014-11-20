package com.iot.raspberry.remote.control.service.impl.adapter.convertor;

import com.iot.raspberry.remote.control.domain.SwitchState;
import com.iot.raspberry.remote.control.service.impl.adapter.PinConfigurationStorage;
import com.pi4j.io.gpio.PinState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapping class between Logical {@link SwitchState} and {@link PinState}
 * @Tip Check the property files in conf directory of the the webapp project for mapping info.
 */
@Component
public class StateMapping {

    @Autowired
    private PinConfigurationStorage pinConfigurationStorage;

    public PinState mapToPinState(int switchId, SwitchState switchState) {
        PinState switchOnPinStateMapping;
        PinState switchOffPinStateMapping;
        if(pinConfigurationStorage.getPin(switchId).getSwitchOnStateMapping()==PinState.LOW) {
            switchOnPinStateMapping = PinState.LOW;
            switchOffPinStateMapping = PinState.HIGH;
        } else {
            switchOnPinStateMapping = PinState.HIGH;
            switchOffPinStateMapping = PinState.LOW;
        }
        return (switchState == SwitchState.OFF) ? switchOffPinStateMapping : switchOnPinStateMapping;
    }

    public SwitchState mapToSwitchState(int switchId, PinState pinState) {
        SwitchState pinLowSwitchStateMapping;
        SwitchState pinHighSwitchStateMapping;
        if(pinConfigurationStorage.getPin(switchId).getSwitchOnStateMapping()==PinState.LOW) {
            pinLowSwitchStateMapping = SwitchState.ON;
            pinHighSwitchStateMapping = SwitchState.OFF;
        } else {
            pinLowSwitchStateMapping = SwitchState.OFF;
            pinHighSwitchStateMapping = SwitchState.ON;
        }
        return (pinState == pinState.LOW) ? pinLowSwitchStateMapping : pinHighSwitchStateMapping;
    }
}
