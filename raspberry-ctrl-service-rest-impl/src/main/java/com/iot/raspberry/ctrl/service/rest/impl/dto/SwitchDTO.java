package com.iot.raspberry.ctrl.service.rest.impl.dto;

/**
 * REST DTO used to represent Power Switches
 */
public class SwitchDTO {

    private int id;
    private String name;
    private SwitchStateDTO switchState;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSwitchState(SwitchStateDTO switchState) {
        this.switchState = switchState;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SwitchStateDTO getSwitchState() {
        return switchState;
    }
}
