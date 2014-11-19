package com.iot.raspberry.remote.control.service.support;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds the Physical configuration of the pins
 * The information is static and loaded from a property file
 *
 * @tip The Pin ID listed in teh property file MUST match the Ids listed in the physical-configuration.properties
 * @Tip Check the property files in conf directory of the the webapp project for more info
 */
@Component
@Configuration
@PropertySource("classpath:/physical-configuration.properties")
public class PinConfigurationStorage {

    public static final String PIN_LIST_KEY = "pins";
    public static final String PIN_PREFIX_KEY = "pin";
    public static final String SPLIT_CHARACTER = ",";

    @Autowired
    private Environment environment;

    private PinState offPinState;

    private PinState onPinState;

    @Autowired
    private RaspberryModelBPinIOConfiguration raspberryModelBPinIOConfiguration;

    private final Map<Integer, Pin> activePins = new HashMap<Integer, Pin>();

    @PostConstruct
    void initializeConfiguration() {

        String pinListProperty = environment.getProperty(PIN_LIST_KEY);
        if (StringUtils.isEmpty(pinListProperty)) {
            throw new IllegalStateException("Configuration error, List of pins not provided... expecting " + PIN_LIST_KEY + "=[Comma separated List of Id] ");
        }
        String[] pinList = environment.getProperty(PIN_LIST_KEY).split(SPLIT_CHARACTER);

        for (int i = 0; i < pinList.length; i++) {

            int pinId;
            try {
                pinId = Integer.parseInt(pinList[i]);
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Configuration error, Pin Id should be numeric");
            }

            String pinName = environment.getProperty(PIN_PREFIX_KEY + "." + pinId);
            if (StringUtils.isEmpty(pinName)) {
                throw new IllegalStateException("Configuration error, Pin [" + pinId + "] present in the list but no detail line could be found... expecting " + PIN_PREFIX_KEY + "." + pinList[i] + "=[GPIO Name] ");
            }

            Pin pin = raspberryModelBPinIOConfiguration.getGPIOPin(pinName);

            if (StringUtils.isEmpty(pin)) {
                throw new IllegalStateException("Configuration error, No physical Pin mapped to [" + pinName + "] check RaspBerry IO Configuration");
            }

            activePins.put(pinId, pin);
        }
    }

    public Pin getPin(int switchId) {
        Pin pin = activePins.get(switchId);
        return pin;
    }

    public List<Pin> getConfiguration() {
        return new ArrayList<Pin>(activePins.values());
    }

}
