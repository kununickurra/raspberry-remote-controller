package com.iot.raspberry.remote.control.service.support;

import com.iot.raspberry.remote.control.domain.SwitchState;
import com.iot.raspberry.remote.control.service.spec.exception.SwitchNotFoundException;
import com.pi4j.io.gpio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tilliern on 5/11/2014.
 */
@Component
/**
 * GPIO adapter using the pi4j libraries to handle GPIO pins. {@link http://pi4j.com/}
 *
 * Initialization of the GPIO pins (Controller + pin provisioning) and the controller is done using the @PostConstruct.
 * Note that the initialization must be performed one and only once inside the instance
 * of the app server. System will fail to start id the GPIO Pins are already provisioned
 *
 * Resources will be cleaned in when destroying (@PreDestroy) the context
 *
 */
public class GpioPinAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(GpioPinAdapter.class);

    @Autowired
    private SwitchStateToPinStateConverter switchStateToPinStateConverter;

    @Autowired
    private PinStateToSwitchStateConverter pinStateToSwitchStateConverter;

    @Autowired
    private GpioControllerFactory gpioControllerFactory;

    @Autowired
    private PinConfigurationStorage pinConfigurationStorage;

    private Map<Pin, GpioPinDigitalOutput> provisionedPins;

    /**
     * Initialize and provision GPIO Pins based on all configured pins.
     */
    @PostConstruct
    void initializePins() {

        LOG.info("Starting provisioning Raspberry GPIO pins...");
        provisionedPins = new HashMap<Pin, GpioPinDigitalOutput>();
        Collection<Pin> configuredPins = pinConfigurationStorage.getConfiguration();

        for (Pin pin : configuredPins) {
            LOG.info("Provisioning GPIO pin {}", pin.getName());
            GpioPinDigitalOutput pinControl = gpioControllerFactory.getInstance().provisionDigitalOutputPin(pin);
            pinControl.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
            provisionedPins.put(pin, pinControl);
            LOG.info("GPIO pin {} provisioned", pin.getName());
        }
        LOG.info("GPIO pin provisioning completed successfully on Raspberry...");
    }

    /**
     * Unprovision GPIO Pins and shutdown the controller.
     */
    @PreDestroy
    void freeResources() {

        LOG.info("Starting unprovision raspberry GPIO pins...");
        GpioController gpioController = gpioControllerFactory.getInstance();
        for (Pin pin : provisionedPins.keySet()) {
            LOG.info("Unprovisioning GPIO pin {}", pin);
            gpioController.unprovisionPin(provisionedPins.get(pin));
            LOG.info("GPIO pin {} unprovisioned", pin);
        }
        LOG.info("Shutting down controller...");
        gpioController.shutdown();
        LOG.info("GPIO Controller is down, fee resources completed successfully.");
    }

    /**
     * Change the Pin state
     *
     * @param switchId Id
     * @param state new state.
     * @throws SwitchNotFoundException in case no configured switch is found using the given id.
     */
    public synchronized void setPinState(int switchId, SwitchState state) throws SwitchNotFoundException {
        PinState newState = switchStateToPinStateConverter.convert(state);
        Pin pin = tryGetPinFromConfiguration(switchId);
        GpioPinDigitalOutput pinControl = provisionedPins.get(pin);
        LOG.info("Changing GPIO pin {} to state {} ...", pin, newState);
        pinControl.setState(newState);
    }

    /**
     * Retrieve Pin state
     * @param switchId Id
     * @return State of the switch that matches the Id provided
     * @throws SwitchNotFoundException in case no configured switch is found using the given id.
     */
    public synchronized SwitchState getPinState(int switchId) throws SwitchNotFoundException {
        Pin pin = tryGetPinFromConfiguration(switchId);
        GpioPinDigitalOutput pinControl = provisionedPins.get(pin);
        PinState state = pinControl.getState();
        LOG.info("Current state of GPIO pin {} is {} ...", pin, state);
        return pinStateToSwitchStateConverter.convert(state);
    }

    private Pin tryGetPinFromConfiguration(int switchId) throws SwitchNotFoundException {
        Pin pin = pinConfigurationStorage.getPin(switchId);
        if (pin == null) {
            throw new SwitchNotFoundException(switchId);
        }
        return pin;
    }
}
