package com.iot.raspberry.ctrl.service.impl.adapter;

import com.iot.raspberry.ctrl.service.impl.adapter.model.RaspberryGpioPin;
import com.iot.raspberry.ctrl.service.impl.adapter.hardware.RaspberryModelBPinIOConfiguration;
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
 * <p>
 *
 * <b>Tip:</b> The Pin ID listed in the property file MUST match the Ids listed in the physical-configuration.properties
 * <br>
 * <b>Tip:</b> Check the property files in conf directory of the the webapp project for more info
 */
@Component
@Configuration
@PropertySource("classpath:/physical-configuration.properties")
public class PinConfigurationStorage {

    private static final String PIN_LIST_KEY = "pins";
    private static final String PIN_PREFIX_KEY = "pin";
    private static final String PIN_GPIO_PREFIX_KEY = "gpio";
    private static final String PIN_ONSTATE_PREFIX_KEY = "state.switch.on";
    private static final String SPLIT_CHARACTER = ",";
    private static final PinState DEFAULT_ON_STATE = PinState.HIGH;

    @Autowired
    private Environment environment;

    @Autowired
    private RaspberryModelBPinIOConfiguration raspberryModelBPinIOConfiguration;

    private PinState offPinState;

    private PinState onPinState;

    private final Map<Integer, RaspberryGpioPin> activePins = new HashMap<Integer, RaspberryGpioPin>();

    @PostConstruct
    void initializeConfiguration() {

        String pinListProperty = environment.getProperty(PIN_LIST_KEY);
        if (StringUtils.isEmpty(pinListProperty)) {
            throw new IllegalStateException("Configuration error, List of pins not provided... expecting " + PIN_LIST_KEY + "=[Comma separated List of Id] ");
        }
        String[] pinList = environment.getProperty(PIN_LIST_KEY).split(SPLIT_CHARACTER);

        for (int i = 0; i < pinList.length; i++) {

            int pinId = tryGetPinId(pinList, i);

            String gpioMappingPropertyName = PIN_PREFIX_KEY + "." + pinId + "." + PIN_GPIO_PREFIX_KEY;
            String pinGpioMapping = environment.getProperty(gpioMappingPropertyName);

            if (StringUtils.isEmpty(pinGpioMapping)) {
                throw new IllegalStateException("Configuration error, Pin [" + pinId + "] present in the list but no detail line could be found... expecting " + gpioMappingPropertyName + "=[GPIO Name] ");
            }

            Pin pin = raspberryModelBPinIOConfiguration.getGPIOPin(pinGpioMapping);

            if (StringUtils.isEmpty(pin)) {
                throw new IllegalStateException("Configuration error, No physical Pin mapped to [" + pinGpioMapping + "] check RaspBerry IO Configuration");
            }

            // Get the SwitchState.ON mapping with the Pin state (LOW/HIGH)
            String gpioPinStateMappingPropertyName = PIN_PREFIX_KEY + "." + pinId + "." + PIN_ONSTATE_PREFIX_KEY;
            String switchOnStateMapping = environment.getProperty(gpioPinStateMappingPropertyName);

            PinState pinState;
            if(StringUtils.isEmpty(switchOnStateMapping)) {
                // Use default configuration
                pinState = DEFAULT_ON_STATE;
            } else {
                try {
                    pinState = PinState.valueOf(switchOnStateMapping);
                } catch (Exception e) {
                    throw new IllegalStateException("Configuration error, PinState [" + switchOnStateMapping + "] does not exists ");
                }
            }

            activePins.put(pinId, new RaspberryGpioPin(pin, pinState));
        }

    }

    private int tryGetPinId(String[] pinList, int i) {
        int pinId;
        try {
            pinId = Integer.parseInt(pinList[i]);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Configuration error, Pin Id should be numeric");
        }
        return pinId;
    }

    public RaspberryGpioPin getPin(int switchId) {
        return activePins.get(switchId);
    }

    public List<RaspberryGpioPin> getConfiguration() {
        return new ArrayList<RaspberryGpioPin>(activePins.values());
    }

}
