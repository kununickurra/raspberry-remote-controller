package com.iot.raspberry.ctrl.dao.impl;

import com.iot.raspberry.ctrl.domain.configuration.PowerSwitchConfiguration;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationHolderTest {

    public static final String SWITCHES_LIST_KEY = "switches";
    public static final String SWITCH_PREFIX_KEY = "switch";

    @Mock
    private Environment environment;

    @InjectMocks
    private ConfigurationHolder configurationHolder = new ConfigurationHolder();

    PowerSwitchConfiguration expected1 = new PowerSwitchConfiguration(1, "First Switch");
    PowerSwitchConfiguration expected2 = new PowerSwitchConfiguration(2, "Second Switch");
    PowerSwitchConfiguration expected3 = new PowerSwitchConfiguration(3, "Third Switch");

    @Test
    public void shouldInitializeConfigurationProperly() {
        // Given
        given(environment.getProperty(SWITCHES_LIST_KEY)).willReturn("1,2,3");
        given(environment.getProperty(SWITCH_PREFIX_KEY + "." + expected1.getId())).willReturn(expected1.getName());
        given(environment.getProperty(SWITCH_PREFIX_KEY + "." + expected2.getId())).willReturn(expected2.getName());
        given(environment.getProperty(SWITCH_PREFIX_KEY + "." + expected3.getId())).willReturn(expected3.getName());
        // When
        configurationHolder.initializeConfiguration();
        // Then
        assertThat(configurationHolder.getConfiguration().size(), is(3));
        assertThat(configurationHolder.getConfiguration(), Matchers.containsInAnyOrder(expected1, expected2, expected3));
    }


    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenSwitchListIsEmpty() {
        // Given
        given(environment.getProperty(SWITCHES_LIST_KEY)).willReturn("");
        // WHen
        configurationHolder.initializeConfiguration();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenSwitchListNull() {
        // Given
        given(environment.getProperty(SWITCHES_LIST_KEY)).willReturn(null);
        // When
        configurationHolder.initializeConfiguration();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenSwitchIsNotNumeric() {
        // Given
        given(environment.getProperty(SWITCHES_LIST_KEY)).willReturn("notNumeric");
        // When
        configurationHolder.initializeConfiguration();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenSwitchNameIsEmpty() {
        // Given
        given(environment.getProperty(SWITCHES_LIST_KEY)).willReturn("1");
        given(environment.getProperty(SWITCH_PREFIX_KEY + "." + expected1.getId())).willReturn("");
        // When
        configurationHolder.initializeConfiguration();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionWhenSwitchNameIsNull() {
        // Given
        given(environment.getProperty("switches")).willReturn("1");
        given(environment.getProperty("switch.1")).willReturn(null);
        // When
        configurationHolder.initializeConfiguration();
    }
}
