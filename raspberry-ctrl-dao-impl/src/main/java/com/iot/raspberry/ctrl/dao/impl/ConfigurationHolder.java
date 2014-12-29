package com.iot.raspberry.ctrl.dao.impl;

import com.iot.raspberry.ctrl.domain.configuration.PowerSwitchConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

/**
 * Holds the configuration for the Power Switch service.
 * The information is static and loaded from a property file
 * <p>
 * <b>Tip:</b> check the different switch-configuration.properties in conf directory of the the webapp project
 */
@Configuration
@PropertySource("classpath:/logical-configuration.properties")
@Component
public class ConfigurationHolder {

    public static final String SWITCHES_LIST_KEY = "switches";
    public static final String SWITCH_PREFIX_KEY = "switch";
    public static final String SPLIT_CHARACTER = ",";

    @Autowired
    private Environment environment;

    private List<PowerSwitchConfiguration> configuration = new LinkedList<PowerSwitchConfiguration>();

    @PostConstruct
    void initializeConfiguration() {
        String switchListProperty = environment.getProperty(SWITCHES_LIST_KEY);
        if (StringUtils.isEmpty(switchListProperty)) {
            throw new IllegalStateException("Configuration error, List of switches not provided... expecting " + SWITCHES_LIST_KEY + "=[Comma separated List of id] ");
        }
        String[] switchList = switchListProperty.split(SPLIT_CHARACTER);
        for (int i = 0; i < switchList.length; i++) {

            int switchId = 0;

            try {
                switchId = Integer.parseInt(switchList[i]);
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Configuration error, Switch Id should be numeric");
            }

            String switchName = environment.getProperty(SWITCH_PREFIX_KEY + "." + switchList[i]);
            if (StringUtils.isEmpty(switchName)) {
                throw new IllegalStateException("Configuration error, Switch [" + switchId + "] present in the list but no detail line could be found... expecting " + SWITCH_PREFIX_KEY + "." + switchList[i] + "=[Switch Name] ");
            }
            configuration.add(
                    new PowerSwitchConfiguration(
                            switchId,
                            switchName));
        }
    }

    public List<PowerSwitchConfiguration> getConfiguration() {
        return configuration;
    }

}
