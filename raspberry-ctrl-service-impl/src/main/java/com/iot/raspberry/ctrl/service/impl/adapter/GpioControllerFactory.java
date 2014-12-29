package com.iot.raspberry.ctrl.service.impl.adapter;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import org.springframework.stereotype.Component;

/**
 * Factory class for the GpioController that allows to mock the Controller
 * by encapsulating the GpioFactory.getInstance() call inside a Spring singleton
 */

@Component
public class GpioControllerFactory {

    private final GpioController gpioController = GpioFactory.getInstance();

    /**
     * @return sole instance of the GpioController.
     */
    public GpioController getInstance() {
        return gpioController;
    }
}
