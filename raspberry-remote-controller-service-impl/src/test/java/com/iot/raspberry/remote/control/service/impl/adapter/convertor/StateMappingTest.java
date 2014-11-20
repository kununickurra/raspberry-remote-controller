package com.iot.raspberry.remote.control.service.impl.adapter.convertor;

import com.iot.raspberry.remote.control.domain.SwitchState;
import com.iot.raspberry.remote.control.service.impl.adapter.PinConfigurationStorage;
import com.iot.raspberry.remote.control.service.impl.adapter.model.RaspberryGpioPin;
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

    @InjectMocks
    private StateMapping stateMapping = new StateMapping();

    @Test
    public void shouldMapPinStateSuccessfullyWithPinStateHighConfiguration() throws Exception {
        given(pinConfigurationStorage.getPin(1)).willReturn(new RaspberryGpioPin(null, PinState.HIGH));
        PinState pinStateSwitchOn= stateMapping.mapToPinState(1, SwitchState.ON);
        PinState pinStateSwitchOff= stateMapping.mapToPinState(1, SwitchState.OFF);
        assertThat(pinStateSwitchOn, is(PinState.HIGH));
        assertThat(pinStateSwitchOff, is(PinState.LOW));
    }

    @Test
    public void shouldMapPinStateSuccessfullyWithPinStateLowConfiguration() throws Exception {
        given(pinConfigurationStorage.getPin(1)).willReturn(new RaspberryGpioPin(null, PinState.LOW));
        PinState pinStateSwitchOn= stateMapping.mapToPinState(1, SwitchState.ON);
        PinState pinStateSwitchOff= stateMapping.mapToPinState(1, SwitchState.OFF);
        assertThat(pinStateSwitchOn, is(PinState.LOW));
        assertThat(pinStateSwitchOff, is(PinState.HIGH));
    }

    @Test
    public void shouldMapSwitchStateSuccessfullyWithPinStateHighConfiguration() throws Exception {
        given(pinConfigurationStorage.getPin(1)).willReturn(new RaspberryGpioPin(null, PinState.HIGH));
        SwitchState switchStatePinHigh= stateMapping.mapToSwitchState(1, PinState.HIGH);
        SwitchState switchStatePinLow= stateMapping.mapToSwitchState(1, PinState.LOW);
        assertThat(switchStatePinHigh, is(SwitchState.ON));
        assertThat(switchStatePinLow, is(SwitchState.OFF));
    }

    @Test
    public void shouldMapSwitchStateSuccessfullyWithPinStateLowConfiguration() throws Exception {
        given(pinConfigurationStorage.getPin(1)).willReturn(new RaspberryGpioPin(null, PinState.LOW));
        SwitchState switchStatePinHigh= stateMapping.mapToSwitchState(1, PinState.HIGH);
        SwitchState switchStatePinLow= stateMapping.mapToSwitchState(1, PinState.LOW);
        assertThat(switchStatePinHigh, is(SwitchState.OFF));
        assertThat(switchStatePinLow, is(SwitchState.ON));
    }

}