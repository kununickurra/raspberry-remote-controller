package com.iot.raspberry.ctrl.dao.spec;

import com.iot.raspberry.ctrl.domain.configuration.PowerSwitchConfiguration;

import java.util.List;

/**
 * Basic DAO interface used to get power switches configuration.
 * Read-Only
 */
public interface PowerSwitchConfigurationDao {
    /**
     * @return List<PowerSwitchConfiguration> containing all configured switches
     */
    public List<PowerSwitchConfiguration> findAll();

    /**
     * @return PowerSwitchConfiguration that match the id
     * or <code>null</code> if no configuration can be found for the given id
     */
    public PowerSwitchConfiguration findById(int id);
}
