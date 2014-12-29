package com.iot.raspberry.ctrl.service.impl.adapter.convertor;

import com.iot.raspberry.ctrl.domain.SwitchState;
import com.iot.raspberry.ctrl.service.impl.adapter.PinConfigurationStorage;
import com.pi4j.io.gpio.PinState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapping class between domain {@link SwitchState} and {@link PinState}
 * <p/>
 * <b>Tip:</b> Check the property files in conf directory of the the webapp project for mapping info.
 */
@Component
public class StateMapping {

    @Autowired
    private PinConfigurationStorage pinConfigurationStorage;

    public PinState mapToPinState(int switchId, SwitchState switchState) {
        PinState switchOnPinStateMapping = pinConfigurationStorage.getPin(switchId).getSwitchOnStateMapping();
        return (switchState == SwitchState.ON ? switchOnPinStateMapping : invert(switchOnPinStateMapping));
    }

    public SwitchState mapToSwitchState(int switchId, PinState pinState) {
        SwitchState pinHighSwitchStateMapping = SwitchState.ON;
        if (pinConfigurationStorage.getPin(switchId).getSwitchOnStateMapping() == PinState.LOW) {
            pinHighSwitchStateMapping = SwitchState.OFF;
        }
        return (pinState == pinState.HIGH) ? pinHighSwitchStateMapping : invert(pinHighSwitchStateMapping);
    }

    private PinState invert(PinState pinState) {
        return (pinState == PinState.LOW ? PinState.HIGH : PinState.LOW);
    }

    private SwitchState invert(SwitchState switchState) {
        return (switchState == SwitchState.OFF ? SwitchState.ON :SwitchState.OFF);
    }
}
