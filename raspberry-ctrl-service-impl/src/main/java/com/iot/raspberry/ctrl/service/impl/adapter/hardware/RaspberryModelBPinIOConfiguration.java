package com.iot.raspberry.ctrl.service.impl.adapter.hardware;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * List of pins that can be used depending on Raspberry Model.
 * <p>
 * <b>Tip</b> Note that this is NOT exhaustive and only contains what we need for now.
 */
@Component
public class RaspberryModelBPinIOConfiguration {

    private static Map<String, Pin> pins = new HashMap<String, Pin>();

    static {
        pins.put("GPIO_00", RaspiPin.GPIO_00);
        pins.put("GPIO_01", RaspiPin.GPIO_01);
        pins.put("GPIO_02", RaspiPin.GPIO_02);
        pins.put("GPIO_03", RaspiPin.GPIO_03);
        pins.put("GPIO_04", RaspiPin.GPIO_04);
    }

    public Pin getGPIOPin(String pinName) {
        return pins.get(pinName);
    }

}