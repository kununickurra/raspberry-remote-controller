package com.iot.raspberry.remote.control.service.impl.adapter;

import com.iot.raspberry.remote.control.domain.SwitchState;
import com.iot.raspberry.remote.control.service.impl.adapter.convertor.StateMapping;
import com.iot.raspberry.remote.control.service.impl.adapter.model.RaspberryGpioPin;
import com.iot.raspberry.remote.control.service.spec.exception.SwitchNotFoundException;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GpioPinAdapterTest {

    @Mock
    private GpioController gpioController;

    @Mock
    private GpioPinDigitalOutput gpioPinDigitalOutput1;

    @Mock
    private GpioPinDigitalOutput gpioPinDigitalOutput2;

    @Mock
    private GpioControllerFactory gpioControllerFactory;

    @Mock
    private PinConfigurationStorage pinConfigurationStorage;

    @Mock
    private StateMapping stateMapping;


    @InjectMocks
    private GpioPinAdapter gpioPinAdapter = new GpioPinAdapter();

    @Test
    public void shouldSetPinStateSuccessFully() throws SwitchNotFoundException {
        // Given
        buildDefaultConfiguration();
        gpioPinAdapter.initializePins();
        given(stateMapping.mapToPinState(1, SwitchState.OFF)).willReturn(PinState.LOW);
        // When
        gpioPinAdapter.setPinState(1, SwitchState.OFF);
        // Then
        verify(gpioPinDigitalOutput1).setState(PinState.LOW);
    }

    @Test(expected = SwitchNotFoundException.class)
    public void shouldThrowPinNotConfiguredExceptionWhenSettingPinState() throws SwitchNotFoundException {
        // Given
        buildDefaultConfiguration();
        gpioPinAdapter.initializePins();
        given(stateMapping.mapToPinState(1, SwitchState.OFF)).willReturn(PinState.LOW);
        given(pinConfigurationStorage.getPin(5)).willReturn(null);
        // When
        gpioPinAdapter.setPinState(5, SwitchState.OFF);
        // Then
    }

    @Test
    public void shouldGetPinStateSuccessFully() throws SwitchNotFoundException {
        // Given
        buildDefaultConfiguration();
        gpioPinAdapter.initializePins();
        given(stateMapping.mapToSwitchState(1, PinState.LOW)).willReturn(SwitchState.ON);
        given(gpioPinDigitalOutput1.getState()).willReturn(PinState.LOW);
        // When
        SwitchState state = gpioPinAdapter.getPinState(1);
        // Then
        assertThat(state, is(SwitchState.ON));
        verify(gpioPinDigitalOutput1,times(1)).getState();
    }

    @Test(expected = SwitchNotFoundException.class)
    public void shouldThrowPinNotConfiguredExceptionWhenGettingPinState() throws SwitchNotFoundException {
        // Given
        buildDefaultConfiguration();
        gpioPinAdapter.initializePins();
        given(stateMapping.mapToSwitchState(5, PinState.LOW)).willReturn(SwitchState.ON);
        given(pinConfigurationStorage.getPin(5)).willReturn(null);
        // When
        SwitchState state = gpioPinAdapter.getPinState(5);
        // Then
    }

    @Test
    public void shouldProvisionPinSuccessFullyWhenInitializing() throws SwitchNotFoundException {
        // Given
        buildDefaultConfiguration();
        // When
        gpioPinAdapter.initializePins();
        // Then
        verify(gpioController,times(1)).provisionDigitalOutputPin(RaspiPin.GPIO_00);
        verify(gpioController,times(1)).provisionDigitalOutputPin(RaspiPin.GPIO_01);
    }

    @Test
    public void shouldUnprovisionPinSuccessFullyWhenShuttingDown() throws SwitchNotFoundException {
        // Given
        buildDefaultConfiguration();
        gpioPinAdapter.initializePins();
        // When
        gpioPinAdapter.freeResources();
        // Then
        verify(gpioController,times(1)).unprovisionPin(gpioPinDigitalOutput1);
        verify(gpioController,times(1)).unprovisionPin(gpioPinDigitalOutput2);
    }

    private void buildDefaultConfiguration() throws SwitchNotFoundException {
        given(gpioControllerFactory.getInstance()).willReturn(gpioController);
        List<RaspberryGpioPin> mockPinList = new ArrayList<RaspberryGpioPin>();
        RaspberryGpioPin pin1 = new RaspberryGpioPin(RaspiPin.GPIO_00, PinState.HIGH);
        RaspberryGpioPin pin2 = new RaspberryGpioPin(RaspiPin.GPIO_01, PinState.HIGH);
        mockPinList.add(pin1);
        mockPinList.add(pin2);
        given(pinConfigurationStorage.getConfiguration()).willReturn(mockPinList);
        given(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00)).willReturn(gpioPinDigitalOutput1);
        given(gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_01)).willReturn(gpioPinDigitalOutput2);
        given(pinConfigurationStorage.getConfiguration()).willReturn(mockPinList);
        given(pinConfigurationStorage.getPin(1)).willReturn(pin1);
    }
}