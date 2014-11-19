package com.iot.raspberry.remote.control.service.rest.impl.controller;

import com.iot.raspberry.remote.control.domain.PowerSwitch;
import com.iot.raspberry.remote.control.domain.SwitchState;
import com.iot.raspberry.remote.control.service.spec.PowerSwitchBoxService;
import com.iot.raspberry.remote.control.service.spec.exception.SwitchNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class SwitchServiceRestControllerTest {

    // TODO: Hypermedia not tested
    // TODO: Check error scenario coverage.

    private static final int FIRST_SWITCH_ID = 1;
    private static final String FIRST_SWITCH_NAME = "switch1";
    private static final SwitchState FIRST_SWITCH_SATE = SwitchState.OFF;

    private static final int SECOND_SWITCH_ID = 2;
    private static final String SECOND_SWITCH_NAME = "switch2";
    private static final SwitchState SECOND_SWITCH_SATE = SwitchState.ON;

    private MockMvc mockMvc;

    @Mock
    private PowerSwitchBoxService powerSwitchBoxService;

    @InjectMocks
    private SwitchServiceRestController testedController = new SwitchServiceRestController();

    @Before
    public void initMocks() throws Exception{
        mockMvc = standaloneSetup(testedController).setHandlerExceptionResolvers(createExceptionResolver()).build();
        initDefaultConfig();
    }

    @Test
    public void shouldGetAllPowerSwitchesSuccessfully() throws Exception {
        given(powerSwitchBoxService.getConfiguredPowerSwitches()).willReturn(Arrays.asList(new PowerSwitch(FIRST_SWITCH_ID, FIRST_SWITCH_NAME, FIRST_SWITCH_SATE),new PowerSwitch(SECOND_SWITCH_ID, SECOND_SWITCH_NAME, SECOND_SWITCH_SATE)));
        // When
        ResultActions result = mockMvc.perform(get("/rest/powerswitch").accept(MediaType.APPLICATION_JSON));
        // Then
        result.andExpect(status().isOk());
        verifyPowerSwitch(result, "content[0].", 1, SwitchState.OFF);
        verifyPowerSwitch(result, "content[1].", 2, SwitchState.ON);
        System.out.println(result);
    }

    @Test
    public void shouldTurnOnSwitchSuccessfully() throws Exception {
        // When
        ResultActions result = mockMvc.perform(put("/rest/powerswitch/1/turnon").accept(MediaType.APPLICATION_JSON));
        // Then
        result.andExpect(status().isNoContent());
        Mockito.verify(powerSwitchBoxService).setSwitchState(1, SwitchState.ON);

        System.out.println(result);
    }

    @Test
    public void shouldTurnOffSwitchSuccessfully() throws Exception {
        // When
        ResultActions result = mockMvc.perform(put("/rest/powerswitch/1/turnoff").accept(MediaType.APPLICATION_JSON));
        // Then
        result.andExpect(status().isNoContent());
        Mockito.verify(powerSwitchBoxService).setSwitchState(1, SwitchState.OFF);

        System.out.println(result);
    }
    @Test
    public void shouldGetPowerSwitchSuccessfully() throws Exception {
        // Given
        ResultActions result = mockMvc.perform(get("/rest/powerswitch/1").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        verifyPowerSwitch(result,"",1,SwitchState.OFF);
        System.out.println(result);
    }

    @Test
    public void shouldReturnErrorWhenGettingNonExistingSwitch() throws Exception {
        ResultActions result = mockMvc.perform(get("/rest/powerswitch/3").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isBadRequest());
        verifySwitchError(result, 1001,3);
    }

    private void verifySwitchError(ResultActions result, int code, int switchId) throws Exception {
        result.andExpect(jsonPath("code").value(code));
        result.andExpect(jsonPath("message").value("PowerSwitch "+switchId+" not found..."));
    }

    private void verifyPowerSwitch(ResultActions result, String prefix, int id, SwitchState expectedState) throws Exception {
        result.andExpect(jsonPath(prefix+"content" + ".id").value(id));
        result.andExpect(jsonPath(prefix+"content" + ".name").value("switch"+id));
        result.andExpect(jsonPath(prefix+"content" + ".switchState").value(expectedState.name()));
    }

    private ExceptionHandlerExceptionResolver createExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(RestErrorHandler.class).resolveMethod(exception);
                return new ServletInvocableHandlerMethod(new RestErrorHandler(), method);
            }
        };
        exceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }

    public void initDefaultConfig() throws SwitchNotFoundException {
        given(powerSwitchBoxService.getPowerSwitch(1)).willReturn(new PowerSwitch(FIRST_SWITCH_ID, FIRST_SWITCH_NAME, FIRST_SWITCH_SATE));
        given(powerSwitchBoxService.getPowerSwitch(2)).willReturn(new PowerSwitch(SECOND_SWITCH_ID, SECOND_SWITCH_NAME, SECOND_SWITCH_SATE));
        given(powerSwitchBoxService.getPowerSwitch(3)).willReturn(null);
        given(powerSwitchBoxService.getConfiguredPowerSwitches()).willReturn(Arrays.asList(new PowerSwitch(FIRST_SWITCH_ID, FIRST_SWITCH_NAME, FIRST_SWITCH_SATE),new PowerSwitch(SECOND_SWITCH_ID, SECOND_SWITCH_NAME, SECOND_SWITCH_SATE)));
    }
}