package com.iot.raspberry.remote.control.service.impl.adapter;

import com.iot.raspberry.remote.control.service.impl.adapter.hardware.RaspberryModelBPinIOConfiguration;
import com.iot.raspberry.remote.control.service.impl.adapter.model.RaspberryGpioPin;
import com.iot.raspberry.remote.control.service.spec.exception.SwitchNotFoundException;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PinConfigurationStorageTest {

    @Mock
    private Environment environment;

    @Mock
    private RaspberryModelBPinIOConfiguration raspberryModelBPinIOConfiguration;

    @InjectMocks
    private PinConfigurationStorage configurationStorage = new PinConfigurationStorage();

    @Test
    public void shouldInitializeConfigurationSuccessfully() throws SwitchNotFoundException {
        // Given
        buildDefaultConfiguration();
        // When
        configurationStorage.initializeConfiguration();
        // Then
        List<RaspberryGpioPin> storedConfigurations = configurationStorage.getConfiguration();
        verifyDefaultConfiguration(storedConfigurations);
    }

    public void shouldReturnPinConfiguration() throws SwitchNotFoundException {
        // Given
        buildDefaultConfiguration();
        configurationStorage.initializeConfiguration();
        // When
        List<RaspberryGpioPin> storedConfigurations = configurationStorage.getConfiguration();
        // Then
        verifyDefaultConfiguration(storedConfigurations);
    }

    @Test
    public void shouldFilterPinById() throws SwitchNotFoundException {
        // Given
        buildDefaultConfiguration();
        configurationStorage.initializeConfiguration();
        // When
        RaspberryGpioPin pin = configurationStorage.getPin(1);
        // Then
        assertThat(pin, (is(new RaspberryGpioPin(RaspiPin.GPIO_00, PinState.HIGH))));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenFilterPinByIdNotFound() throws SwitchNotFoundException {

        // Given
        buildDefaultConfiguration();
        given(raspberryModelBPinIOConfiguration.getGPIOPin("GPIO_O1")).willReturn(null);
        configurationStorage.initializeConfiguration();
        // When
        configurationStorage.getPin(5);
        // Then
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenInitializingGIOPinThatDoesNotExists() {
        try {
            // Given
            buildDefaultConfiguration();
            given(raspberryModelBPinIOConfiguration.getGPIOPin("GPIO_O1")).willReturn(null);
            // When
            configurationStorage.initializeConfiguration();
        } finally {
            // Then
            verify(raspberryModelBPinIOConfiguration, never()).getGPIOPin("GPIO_02");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenPinListIsEmpty() {
        // Given
        given(environment.getProperty("pins")).willReturn("");
        // When
        configurationStorage.initializeConfiguration();
        // Then
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenPinListNull() {
        // Given
        given(environment.getProperty("pins")).willReturn(null);
        // When
        configurationStorage.initializeConfiguration();
        // Then
   }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenPinIdIsNotNumeric() {
        // Given
        given(environment.getProperty("pins")).willReturn("notNumeric");
        // When
        configurationStorage.initializeConfiguration();
        // Then
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenPinNameIsEmpty() {
        // Given
        buildDefaultConfiguration();
        given(environment.getProperty("pin.2.gpio")).willReturn("");
        // When
        configurationStorage.initializeConfiguration();
        // Then
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenPinGpioMappingIsNull() {
        // Given
        buildDefaultConfiguration();
        given(environment.getProperty("pin.2.gpio")).willReturn(null);
        // When
        configurationStorage.initializeConfiguration();
        // Then
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenPinStateMappingIsWrong() {
        // Given
        buildDefaultConfiguration();
        given(environment.getProperty("pin.2.state.switch.on")).willReturn("NotAValidState");
        // When
        configurationStorage.initializeConfiguration();
        // Then
    }

    private void verifyDefaultConfiguration(List<RaspberryGpioPin> storedConfigurations) throws SwitchNotFoundException {
        assertThat(storedConfigurations.size(), is(3));
        assertThat(storedConfigurations, Matchers.containsInAnyOrder(
                new RaspberryGpioPin(RaspiPin.GPIO_00, PinState.HIGH),
                new RaspberryGpioPin(RaspiPin.GPIO_01, PinState.LOW),
                new RaspberryGpioPin(RaspiPin.GPIO_02, PinState.HIGH)));
    }

    private void buildDefaultConfiguration() {
        given(environment.getProperty("pins")).willReturn("1,2,3");
        given(environment.getProperty("pin.1.gpio")).willReturn("GPIO_OO");
        given(environment.getProperty("pin.2.gpio")).willReturn("GPIO_O1");
        given(environment.getProperty("pin.3.gpio")).willReturn("GPIO_O2");
        given(environment.getProperty("pin.2.state.switch.on")).willReturn("LOW");
        given(raspberryModelBPinIOConfiguration.getGPIOPin("GPIO_OO")).willReturn(RaspiPin.GPIO_00);
        given(raspberryModelBPinIOConfiguration.getGPIOPin("GPIO_O1")).willReturn(RaspiPin.GPIO_01);
        given(raspberryModelBPinIOConfiguration.getGPIOPin("GPIO_O2")).willReturn(RaspiPin.GPIO_02);

    }
}