package com.iot.raspberry.ctrl.dao.impl;

import com.iot.raspberry.ctrl.domain.configuration.PowerSwitchConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PowerSwitchConfigurationDaoImplTest {

    private static final PowerSwitchConfiguration firstSwitch = new PowerSwitchConfiguration(1, "First Switch");
    private static final PowerSwitchConfiguration secondSwitch = new PowerSwitchConfiguration(2, "Second Switch");
    private static final PowerSwitchConfiguration thirdSwitch = new PowerSwitchConfiguration(3, "Third Switch");

    @Mock
    private ConfigurationHolder configurationHolder;

    @InjectMocks
    private PowerSwitchConfigurationDaoImpl dao = new PowerSwitchConfigurationDaoImpl();

    @Test
    public void shouldReturnAllConfiguredSwitches() {
        // Given
        List<PowerSwitchConfiguration> expectedList = Collections.emptyList();
        given(configurationHolder.getConfiguration()).willReturn(expectedList);
        // When
        List<PowerSwitchConfiguration> returnedList = dao.findAll();
        // Then
        assertThat(returnedList, is(expectedList));
    }

    @Test
    public void shouldFilterConfiguredSwitchedById() {
        // Given
        buildDefaultConfiguration();
        // When
        PowerSwitchConfiguration returnedPowerSwitchConfiguration = dao.findById(2);
        // Then
        assertThat(returnedPowerSwitchConfiguration, is(secondSwitch));
    }

    @Test
    public void shouldReturnNullWhenFilterConfiguredHasNoMatchingPowerSwitch() {
        // Given
        buildDefaultConfiguration();
        // When
        PowerSwitchConfiguration returnedPowerSwitchConfiguration = dao.findById(100);
        // Then
        assertNull(returnedPowerSwitchConfiguration);
    }

    private void buildDefaultConfiguration() {

        List<PowerSwitchConfiguration> configuredSwitches = new ArrayList<PowerSwitchConfiguration>();
        configuredSwitches.add(firstSwitch);
        configuredSwitches.add(secondSwitch);
        configuredSwitches.add(thirdSwitch);
        given(configurationHolder.getConfiguration()).willReturn(configuredSwitches);

    }
}
