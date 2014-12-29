package com.iot.raspberry.ctrl.service.impl;

import com.iot.raspberry.ctrl.dao.spec.PowerSwitchConfigurationDao;
import com.iot.raspberry.ctrl.domain.PowerSwitch;
import com.iot.raspberry.ctrl.domain.SwitchState;
import com.iot.raspberry.ctrl.domain.configuration.PowerSwitchConfiguration;
import com.iot.raspberry.ctrl.service.spec.PowerSwitchBoxService;
import com.iot.raspberry.ctrl.service.spec.exception.SwitchNotFoundException;
import com.iot.raspberry.ctrl.service.impl.adapter.GpioPinAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link PowerSwitchBoxService}.
 * Use the local configuration and the {@link GpioPinAdapter} to manages switches.
 */
@Component
public class PowerSwitchBoxServiceImpl implements PowerSwitchBoxService {

    private static final Logger LOG = LoggerFactory.getLogger(PowerSwitchBoxServiceImpl.class);

    @Autowired
    private GpioPinAdapter gpioPinAdapter;

    @Autowired
    private PowerSwitchConfigurationDao powerSwitchConfigurationDao;

    public List<PowerSwitch> getConfiguredPowerSwitches() {
        List<PowerSwitch> returnedList = new ArrayList<PowerSwitch>();
        List<PowerSwitchConfiguration> powerSwitchConfigurations = powerSwitchConfigurationDao.findAll();

        try {
            for (PowerSwitchConfiguration powerSwitchConfiguration : powerSwitchConfigurations) {
                returnedList.add(getStateAndBuildSwitch(powerSwitchConfiguration));
            }
        } catch (SwitchNotFoundException e) {
            throw new IllegalStateException("Configuration error !", e);
        }
        return returnedList;
    }

    public PowerSwitch getPowerSwitch(int switchId) throws SwitchNotFoundException {
        PowerSwitchConfiguration powerSwitchConfiguration = powerSwitchConfigurationDao.findById(switchId);
        PowerSwitch powerSwitch = null;
        if (powerSwitchConfiguration != null) {
            powerSwitch = getStateAndBuildSwitch(powerSwitchConfiguration);
        }
        return powerSwitch;

    }

    public void setSwitchState(int switchId, SwitchState state) throws SwitchNotFoundException {
        PowerSwitch powerSwitch = getPowerSwitch(switchId);
        changeSwitchState(powerSwitch, state);
    }

    private void changeSwitchState(PowerSwitch powerSwitch, SwitchState newState) throws SwitchNotFoundException {
        LOG.info("Stitching state of switch {} to {}", powerSwitch, newState);
        gpioPinAdapter.setPinState(powerSwitch.getId(), newState);
    }

    private PowerSwitch getStateAndBuildSwitch(PowerSwitchConfiguration powerSwitchConfiguration) throws SwitchNotFoundException {
        SwitchState state = gpioPinAdapter.getPinState(powerSwitchConfiguration.getId());
        return new PowerSwitch(powerSwitchConfiguration.getId(), powerSwitchConfiguration.getName(), state);
    }
}
