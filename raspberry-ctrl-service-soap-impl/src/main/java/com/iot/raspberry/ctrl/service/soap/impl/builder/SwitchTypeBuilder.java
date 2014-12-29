package com.iot.raspberry.ctrl.service.soap.impl.builder;

import com.iot.raspberry.remote.control.service.soap.spec.dto.PowerSwitchType;
import com.iot.raspberry.remote.control.service.soap.spec.dto.SwitchState;

/**
 * Created by tilliern on 30/10/2014.
 */
public class SwitchTypeBuilder {

    private int id;
    private String name;
    private SwitchState switchState;

    public SwitchTypeBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public SwitchTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SwitchTypeBuilder withSwitchState(SwitchState switchState) {
        this.switchState = switchState;
        return this;
    }

    public PowerSwitchType build() {
        return new PowerSwitchType(id, name, switchState);
    }

}
