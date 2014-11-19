package com.iot.raspberry.remote.control.service.simulator;

import com.iot.raspberry.remote.control.dao.spec.PowerSwitchConfigurationDao;
import com.iot.raspberry.remote.control.domain.PowerSwitch;
import com.iot.raspberry.remote.control.domain.SwitchState;
import com.iot.raspberry.remote.control.domain.configuration.PowerSwitchConfiguration;
import com.iot.raspberry.remote.control.service.spec.PowerSwitchBoxService;
import com.iot.raspberry.remote.control.service.spec.exception.SwitchNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple mock service for the {@link PowerSwitchBoxService} used to test the web services
 * outside the raspberry environment.
 *
 * @tip use maven "simulator" profile to deploy the simulator
 *
 */
@Component
public class SwitchBoxServiceSimulator implements PowerSwitchBoxService {

    //TODO: This class is not tested

    private static final Logger LOG = LoggerFactory.getLogger(SwitchBoxServiceSimulator.class);

    @Autowired
    private PowerSwitchConfigurationDao powerSwitchConfigurationDao;

    private static Map<Integer, PowerSwitch> configuredSwitches;

    @PostConstruct
    public void initializeSwitches() {
        LOG.info("Starting simulator...");
        // Make sure that the simulator is initialized once.
        if(configuredSwitches==null) {
            configuredSwitches = new LinkedHashMap<Integer, PowerSwitch>();
            for (PowerSwitchConfiguration powerSwitchConfiguration : powerSwitchConfigurationDao.findAll()) {
                configuredSwitches.put(powerSwitchConfiguration.getId(), new PowerSwitch(powerSwitchConfiguration.getId(), powerSwitchConfiguration.getName(), SwitchState.OFF));
            }
        } else {
            throw new IllegalStateException("Already initialized !!!");
        }
    }

    @PreDestroy
    private void simulatorDestroyed() {
        LOG.info("Stopping simulator...");
    }

    @Override
    public PowerSwitch getPowerSwitch(int switchId) throws SwitchNotFoundException {
        return tryGetSwitch(switchId);
    }

    @Override
    public List<PowerSwitch> getConfiguredPowerSwitches() {
        return new ArrayList<PowerSwitch>(configuredSwitches.values());
    }

    @Override
    public void setSwitchState(int switchId, SwitchState state) throws SwitchNotFoundException {
        PowerSwitch powerSwitch = tryGetSwitch(switchId);
        configuredSwitches.put(switchId, new PowerSwitch(powerSwitch.getId(), powerSwitch.getName(), state));
    }

    private PowerSwitch tryGetSwitch(int switchId) throws SwitchNotFoundException {
        PowerSwitch returnedSwitch = configuredSwitches.get(switchId);
        if(returnedSwitch==null) {
            throw new SwitchNotFoundException(switchId);
        }
        return returnedSwitch;
    }

}
