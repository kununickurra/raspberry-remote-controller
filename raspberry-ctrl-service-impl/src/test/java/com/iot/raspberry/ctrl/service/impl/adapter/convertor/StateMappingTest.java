package com.iot.raspberry.ctrl.service.impl.adapter.convertor;

import com.iot.raspberry.ctrl.domain.SwitchState;
import com.iot.raspberry.ctrl.service.impl.adapter.PinConfigurationStorage;
import com.iot.raspberry.ctrl.service.impl.adapter.convertor.StateMapping;
import com.iot.raspberry.ctrl.service.impl.adapter.model.RaspberryGpioPin;
import com.pi4j.io.gpio.PinState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class StateMappingTest {

    @Mock
    private PinConfigurationStorage pinConfigurationStorage;

    @Mock
    private RaspberryGpioPin raspberryGpioPin;

    @InjectMocks
    private StateMapping stateMapping = new StateMapping();

    @Test
    public void shouldMapPinStateSuccessfullyWithPinStateHighConfiguration() throws Exception {
        // Given
        given(raspberryGpioPin.getSwitchOnStateMapping()).willReturn(PinState.HIGH);
        given(pinConfigurationStorage.getPin(1)).willReturn(raspberryGpioPin);
        // When
        PinState pinStateSwitchOn = stateMapping.mapToPinState(1, SwitchState.ON);
        PinState pinStateSwitchOff = stateMapping.mapToPinState(1, SwitchState.OFF);
        // Then
        assertThat(pinStateSwitchOn, is(PinState.HIGH));
        assertThat(pinStateSwitchOff, is(PinState.LOW));
    }

    @Test
    public void shouldMapPinStateSuccessfullyWithPinStateLowConfiguration() throws Exception {
        // Given
        given(raspberryGpioPin.getSwitchOnStateMapping()).willReturn(PinState.LOW);
        given(pinConfigurationStorage.getPin(1)).willReturn(raspberryGpioPin);
        // When
        PinState pinStateSwitchOn = stateMapping.mapToPinState(1, SwitchState.ON);
        PinState pinStateSwitchOff = stateMapping.mapToPinState(1, SwitchState.OFF);
        // Then
        assertThat(pinStateSwitchOn, is(PinState.LOW));
        assertThat(pinStateSwitchOff, is(PinState.HIGH));
    }

    @Test
    public void shouldMapSwitchStateSuccessfullyWithPinStateHighConfiguration() throws Exception {
        // Given
        given(raspberryGpioPin.getSwitchOnStateMapping()).willReturn(PinState.HIGH);
        given(pinConfigurationStorage.getPin(1)).willReturn(raspberryGpioPin);
        // When
        SwitchState switchStatePinHigh = stateMapping.mapToSwitchState(1, PinState.HIGH);
        SwitchState switchStatePinLow = stateMapping.mapToSwitchState(1, PinState.LOW);
        // Then
        assertThat(switchStatePinHigh, is(SwitchState.ON));
        assertThat(switchStatePinLow, is(SwitchState.OFF));
    }

    @Test
    public void shouldMapSwitchStateSuccessfullyWithPinStateLowConfiguration() throws Exception {
        // Given
        given(raspberryGpioPin.getSwitchOnStateMapping()).willReturn(PinState.LOW);
        given(pinConfigurationStorage.getPin(1)).willReturn(raspberryGpioPin);
        // When
        SwitchState switchStatePinHigh = stateMapping.mapToSwitchState(1, PinState.HIGH);
        SwitchState switchStatePinLow = stateMapping.mapToSwitchState(1, PinState.LOW);
        // Then
        assertThat(switchStatePinHigh, is(SwitchState.OFF));
        assertThat(switchStatePinLow, is(SwitchState.ON));
    }

}