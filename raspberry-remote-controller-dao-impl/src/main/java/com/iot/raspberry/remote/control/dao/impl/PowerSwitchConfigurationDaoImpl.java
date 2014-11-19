package com.iot.raspberry.remote.control.dao.impl;

import com.iot.raspberry.remote.control.dao.spec.PowerSwitchConfigurationDao;
import com.iot.raspberry.remote.control.domain.configuration.PowerSwitchConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
/**
 * Basic implementation of the PowerSwitchConfigurationDao.
 * Configuration is held by the ConfigurationLoader and is read only.
 */
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
