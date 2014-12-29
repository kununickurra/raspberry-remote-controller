package com.iot.raspberry.ctrl.service.soap.impl;

import com.iot.raspberry.ctrl.domain.PowerSwitch;
import com.iot.raspberry.ctrl.domain.SwitchState;
import com.iot.raspberry.remote.control.service.soap.spec.dto.*;
import com.iot.raspberry.ctrl.service.spec.PowerSwitchBoxService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.iot.raspberry.ctrl.service.soap.impl.PowersSwitchTestFixture.getTestPowerSwitchById;
import static com.iot.raspberry.ctrl.service.soap.impl.PowersSwitchTestFixture.getTestPowerSwitches;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PowerSwitchSOAPServiceImplTest {

    //TODO: Verify test coverage, failures...

    @Mock
    private PowerSwitchBoxService switchService;

    @InjectMocks
    private PowerSwitchSOAPServiceImpl powerSwitchSOAPService;

    @Test
    public void shouldGetAllPowerSwitchesSuccessfully() throws Exception {
        given(switchService.getConfiguredPowerSwitches()).willReturn(getTestPowerSwitches());
        GetAllPowerSwitchesResponseDTO dto = powerSwitchSOAPService.getAllPowerSwitches();
        verifySwitchList(dto.getSwitchesTypes(), getTestPowerSwitches().size());
    }

    @Test
    public void testSetAllSwitchesStateSuccessfully() throws Exception {
        given(switchService.getConfiguredPowerSwitches()).willReturn(getTestPowerSwitches());
        SetAllSwitchesStateRequestDTO requestDTO = new SetAllSwitchesStateRequestDTO();
        requestDTO.setSwitchState(com.iot.raspberry.remote.control.service.soap.spec.dto.SwitchState.ON);
        powerSwitchSOAPService.setAllSwitchesState(requestDTO);
        verify(switchService).setSwitchState(1, SwitchState.ON);
        verify(switchService).setSwitchState(2, SwitchState.ON);
    }

    @Test
    public void shouldGetPowerSwitchById() throws Exception {
        given(switchService.getPowerSwitch(1)).willReturn(getTestPowerSwitchById(1));
        GetPowerSwitchByIdRequestDTO requestDto = new GetPowerSwitchByIdRequestDTO();
        requestDto.setId(1);
        GetPowerSwitchByIdResponseDTO response = powerSwitchSOAPService.getPowerSwitchById(requestDto);
        verifySwitch(response.getSwitchType());
    }

    @Test
    public void shouldSetStateSuccessfully() throws Exception {
        SetSwitchStateRequestDTO requestDTO = new SetSwitchStateRequestDTO();
        requestDTO.setStatus(com.iot.raspberry.remote.control.service.soap.spec.dto.SwitchState.ON);
        requestDTO.setId(1);
        powerSwitchSOAPService.setState(requestDTO);
        verify(switchService).setSwitchState(1, SwitchState.ON);
    }

    private void verifySwitchList(List<PowerSwitchType> listToCheck, int size) {
        assertThat(listToCheck.size(), is(size));
        for(PowerSwitchType item : listToCheck) {
            verifySwitch(item);
        }
    }

    private void verifySwitch(PowerSwitchType actualDto) {
       PowerSwitch expectedDto = getTestPowerSwitchById(actualDto.getId());
       assertNotNull(expectedDto);
       assertThat(actualDto.getName(), is(expectedDto.getName()));
       assertThat(actualDto.getStatus().value(), is(expectedDto.getState().name()));
    }


}