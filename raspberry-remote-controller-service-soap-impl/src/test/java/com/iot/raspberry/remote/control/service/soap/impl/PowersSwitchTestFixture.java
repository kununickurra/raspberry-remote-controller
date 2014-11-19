package com.iot.raspberry.remote.control.service.soap.impl;

import com.iot.raspberry.remote.control.domain.PowerSwitch;
import com.iot.raspberry.remote.control.domain.SwitchState;

import java.util.*;

/**
 * Created by tilliern on 18/11/2014.
 */
public class PowersSwitchTestFixture {

    private static final int FIRST_SWITCH_ID = 1;
    private static final String FIRST_SWITCH_NAME = "switch1";
    private static final SwitchState FIRST_SWITCH_SATE = SwitchState.OFF;

    private static final int SECOND_SWITCH_ID = 2;
    private static final String SECOND_SWITCH_NAME = "switch2";
    private static final SwitchState SECOND_SWITCH_SATE = SwitchState.ON;

    private static Map<Integer, PowerSwitch> testSwitches;

    static {
        testSwitches = new HashMap<Integer, PowerSwitch>();
        testSwitches.put(1, new PowerSwitch(FIRST_SWITCH_ID, FIRST_SWITCH_NAME, FIRST_SWITCH_SATE));
        testSwitches.put(2, new PowerSwitch(SECOND_SWITCH_ID, SECOND_SWITCH_NAME, SECOND_SWITCH_SATE));
    }

    public static List<PowerSwitch> getTestPowerSwitches() {
        return new ArrayList<PowerSwitch>(testSwitches.values());
    }

    public static PowerSwitch getTestPowerSwitchById(int id) {
        return testSwitches.get(id);
    }

}
