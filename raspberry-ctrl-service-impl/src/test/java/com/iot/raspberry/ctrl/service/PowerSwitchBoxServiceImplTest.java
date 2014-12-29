package com.iot.raspberry.ctrl.service;

import com.iot.raspberry.ctrl.dao.spec.PowerSwitchConfigurationDao;
import com.iot.raspberry.ctrl.domain.PowerSwitch;
import com.iot.raspberry.ctrl.domain.SwitchState;
import com.iot.raspberry.ctrl.domain.configuration.PowerSwitchConfiguration;
import com.iot.raspberry.ctrl.service.impl.PowerSwitchBoxServiceImpl;
import com.iot.raspberry.ctrl.service.impl.adapter.GpioPinAdapter;
import com.iot.raspberry.ctrl.service.spec.exception.SwitchNotFoundException;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PowerSwitchBoxServiceImplTest {

    @Mock
    private GpioPinAdapter gpioPinAdapter;

    @Mock
    private PowerSwitchConfigurationDao powerSwitchConfigurationDao;

    @InjectMocks
    private PowerSwitchBoxServiceImpl powerSwitchBoxService = new PowerSwitchBoxServiceImpl();

    @Test
    public void shouldGetPowerSwitchSuccessfully() throws SwitchNotFoundException {
        // Given
        PowerSwitchConfiguration powerSwitchConfiguration = new PowerSwitchConfiguration(1, "name");
        PowerSwitch expected = new PowerSwitch(powerSwitchConfiguration.getId(), powerSwitchConfiguration.getName(), SwitchState.OFF);
        given(powerSwitchConfigurationDao.findById(1)).willReturn(powerSwitchConfiguration);
        given(gpioPinAdapter.getPinState(1)).willReturn(expected.getState());
        // When
        PowerSwitch actual = powerSwitchBoxService.getPowerSwitch(1);
        // Then
        assertThat(actual, is(expected));
        verify(powerSwitchConfigurationDao).findById(1);
        verify(gpioPinAdapter).getPinState(1);
    }

    @Test(expected = SwitchNotFoundException.class)
    public void shouldThrowSwitchNotFoundExceptionWhenGettingNotExistingSwitchState() throws SwitchNotFoundException {
        // Given
        PowerSwitchConfiguration powerSwitchConfiguration = new PowerSwitchConfiguration(1, "name");
        PowerSwitch expected = new PowerSwitch(powerSwitchConfiguration.getId(), powerSwitchConfiguration.getName(), SwitchState.OFF);
        given(powerSwitchConfigurationDao.findById(1)).willReturn(powerSwitchConfiguration);
        given(gpioPinAdapter.getPinState(1)).willThrow(new SwitchNotFoundException(1));
        // When
        powerSwitchBoxService.getPowerSwitch(1);
        // Then
    }

    @Test
    public void shouldSetSwitchStateSuccessfully() throws SwitchNotFoundException {
        // Given
        PowerSwitchConfiguration powerSwitchConfiguration = new PowerSwitchConfiguration(1, "name");
        given(powerSwitchConfigurationDao.findById(1)).willReturn(powerSwitchConfiguration);
        // When
        powerSwitchBoxService.setSwitchState(1, SwitchState.OFF);
        // Then
        verify(gpioPinAdapter).setPinState(1, SwitchState.OFF);
    }

    @Test(expected = SwitchNotFoundException.class)
    public void shouldThrowSwitchNotFoundExceptionWhenSettingNotExistingSwitchState() throws SwitchNotFoundException {
        // Given
        PowerSwitchConfiguration powerSwitchConfiguration = new PowerSwitchConfiguration(1, "name");
        given(powerSwitchConfigurationDao.findById(1)).willReturn(powerSwitchConfiguration);
        given(powerSwitchBoxService.getPowerSwitch(1)).willThrow(new SwitchNotFoundException(1));
        // When
        powerSwitchBoxService.setSwitchState(1, SwitchState.OFF);
        // Then
    }

    @Test
    public void shouldGetConfiguredPowerSwitchesSuccessfully() throws SwitchNotFoundException {
        // Given
        PowerSwitchConfiguration powerSwitchConfiguration1 = new PowerSwitchConfiguration(1, "name 1");
        PowerSwitchConfiguration powerSwitchConfiguration2 = new PowerSwitchConfiguration(2, "name 2");
        PowerSwitch expected1 = new PowerSwitch(powerSwitchConfiguration1.getId(), powerSwitchConfiguration1.getName(), SwitchState.OFF);
        PowerSwitch expected2 = new PowerSwitch(powerSwitchConfiguration2.getId(), powerSwitchConfiguration2.getName(), SwitchState.ON);
        given(powerSwitchConfigurationDao.findAll()).willReturn(Arrays.asList(powerSwitchConfiguration1, powerSwitchConfiguration2));
        given(gpioPinAdapter.getPinState(1)).willReturn(expected1.getState());
        given(gpioPinAdapter.getPinState(2)).willReturn(expected2.getState());
        // When
        List<PowerSwitch> actual = powerSwitchBoxService.getConfiguredPowerSwitches();
        // Then
        assertThat(actual.size(), is(2));
        assertThat(actual, Matchers.containsInAnyOrder(expected1, expected2));
        verify(powerSwitchConfigurationDao).findAll();
        verify(gpioPinAdapter).getPinState(1);
        verify(gpioPinAdapter).getPinState(2);
    }
}