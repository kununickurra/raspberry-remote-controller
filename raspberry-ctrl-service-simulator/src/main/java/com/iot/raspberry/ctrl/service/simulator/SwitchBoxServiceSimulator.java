package com.iot.raspberry.ctrl.service.simulator;

import com.iot.raspberry.ctrl.dao.spec.PowerSwitchConfigurationDao;
import com.iot.raspberry.ctrl.domain.PowerSwitch;
import com.iot.raspberry.ctrl.domain.SwitchState;
import com.iot.raspberry.ctrl.domain.configuration.PowerSwitchConfiguration;
import com.iot.raspberry.ctrl.service.spec.PowerSwitchBoxService;
import com.iot.raspberry.ctrl.service.spec.exception.SwitchNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

/**
 * Simple mock service for the {@link PowerSwitchBoxService} used to test the web services
 * outside the raspberry environment. (faster than a simulator and only need a web server)
 * <p>
 * tip use maven "simulator" profile to deploy a version with the simulator enabled
 * <p>
 * This implementation is <b>NOT thread safe</b>.
 *
 */
@Component
public class SwitchBoxServiceSimulator implements PowerSwitchBoxService {

    //TODO: This class is not tested
    //TODO: Thread safety

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
                configuredSwitches.put(
                        powerSwitchConfiguration.getId(),
                        new PowerSwitch(
                                powerSwitchConfiguration.getId(),
                                powerSwitchConfiguration.getName(),
                                SwitchState.OFF));
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
