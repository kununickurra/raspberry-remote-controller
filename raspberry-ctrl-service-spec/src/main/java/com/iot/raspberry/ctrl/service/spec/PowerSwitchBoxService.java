package com.iot.raspberry.ctrl.service.spec;

import com.iot.raspberry.ctrl.domain.PowerSwitch;
import com.iot.raspberry.ctrl.domain.SwitchState;
import com.iot.raspberry.ctrl.service.spec.exception.SwitchNotFoundException;

import java.util.List;

/**
 * Service used expose the Switch Box functionality that allow to manage the state of configured power switches.
 */
public interface PowerSwitchBoxService {

    /**
     * @return List<PowerSwitch> all Configured Power switches available.
     */
    public List<PowerSwitch> getConfiguredPowerSwitches();

    /**
     * @return The PowerSwitch that matches the id provided
     * @throws SwitchNotFoundException in case no configured power switch matches the id provided.
     */
    public PowerSwitch getPowerSwitch(int powerSwitchId) throws SwitchNotFoundException;

    /**
     * Change the state of a given switch
     *
     * @param powerSwitchId id of the switch to set.
     * @param state         the new state the power switch should be set to.
     * @throws SwitchNotFoundException in case no configured power switch matches the id provided.
     */
    public void setSwitchState(int powerSwitchId, SwitchState state) throws SwitchNotFoundException;

}
