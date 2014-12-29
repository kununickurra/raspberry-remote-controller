package com.iot.raspberry.ctrl.service.spec.exception;

/**
 * Exception thrown when a PowerSwitch is not found.
 */
public class SwitchNotFoundException extends Exception {

    private int switchId;

    public SwitchNotFoundException(int switchId) {
        super();
        this.switchId = switchId;
    }

    public int getSwitchId() {
        return switchId;
    }
}
