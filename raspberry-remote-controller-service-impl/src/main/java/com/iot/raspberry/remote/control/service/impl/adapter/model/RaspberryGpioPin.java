package com.iot.raspberry.remote.control.service.impl.adapter.model;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

/**
 * Created by tilliern on 19/11/2014.
 */
public class RaspberryGpioPin {

    private Pin pin;
    private PinState switchOnStateMapping;

    public RaspberryGpioPin(Pin pin, PinState switchOnStateMapping) {
        this.pin = pin;
        this.switchOnStateMapping = switchOnStateMapping;
    }

    public Pin getPin() {
        return pin;
    }

    public PinState getSwitchOnStateMapping() {
        return switchOnStateMapping;
    }

    @Override
    public String toString() {
        return "RaspberryGpioPin{" +
                "pin=" + pin +
                ", switchOnStateMapping=" + switchOnStateMapping +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RaspberryGpioPin)) return false;

        RaspberryGpioPin that = (RaspberryGpioPin) o;

        if (pin != null ? !pin.equals(that.pin) : that.pin != null) return false;
        if (switchOnStateMapping != that.switchOnStateMapping) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pin != null ? pin.hashCode() : 0;
        result = 31 * result + (switchOnStateMapping != null ? switchOnStateMapping.hashCode() : 0);
        return result;
    }
}
