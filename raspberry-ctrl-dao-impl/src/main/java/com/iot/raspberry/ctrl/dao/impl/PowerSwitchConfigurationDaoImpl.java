package com.iot.raspberry.ctrl.dao.impl;

import com.iot.raspberry.ctrl.dao.spec.PowerSwitchConfigurationDao;
import com.iot.raspberry.ctrl.domain.configuration.PowerSwitchConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Basic implementation of the PowerSwitchConfigurationDao.
 * Configuration is held by the ConfigurationLoader and is read only.
 */
@Component
public class PowerSwitchConfigurationDaoImpl implements PowerSwitchConfigurationDao {

    @Autowired
    private ConfigurationHolder configurationHolder;

    @Override
    public List<PowerSwitchConfiguration> findAll() {
        return configurationHolder.getConfiguration();
    }

    @Override
    public PowerSwitchConfiguration findById(int id) {
        for (PowerSwitchConfiguration configuration : configurationHolder.getConfiguration()) {
            if (configuration.getId() == id) {
                return configuration;
            }
        }
        return null;
    }
}
