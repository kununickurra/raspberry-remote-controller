package com.iot.raspberry.remote.control.service.soap.impl;

import com.iot.raspberry.remote.control.domain.PowerSwitch;
import com.iot.raspberry.remote.control.domain.SwitchState;
import com.iot.raspberry.remote.control.service.soap.impl.builder.SwitchTypeBuilder;
import com.iot.raspberry.remote.control.service.soap.spec.dto.*;
import com.iot.raspberry.remote.control.service.spec.PowerSwitchBoxService;
import com.iot.raspberry.remote.control.service.spec.exception.SwitchNotFoundException;
import com.iot.service.powerswitch.PowerSwitchBoxSOAPService;
import com.iot.service.powerswitch.PowerSwitchNotConfiguredFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.List;

/**
 * Soap service implementation of the Power Switch Box.
 */
public class PowerSwitchSOAPServiceImpl implements PowerSwitchBoxSOAPService {

    private static final Logger LOG = LoggerFactory.getLogger(PowerSwitchSOAPServiceImpl.class);

    @Autowired
    private PowerSwitchBoxService switchService;

    @Override
    public GetAllPowerSwitchesResponseDTO getAllPowerSwitches() throws PowerSwitchNotConfiguredFault {

        List<PowerSwitchType> returnedList = new ArrayList<PowerSwitchType>();
        List<PowerSwitch> powerSwitches = switchService.getConfiguredPowerSwitches();

        for (PowerSwitch item : powerSwitches) {
            returnedList.add(buildSwitchTypeDto(item));
        }
        GetAllPowerSwitchesResponseDTO answer = new GetAllPowerSwitchesResponseDTO(returnedList);
        return answer;
    }

    @Override
    public void setAllSwitchesState(@WebParam(partName = "parameters", name = "setAllSwitchesStateRequest", targetNamespace = "http://service.iot.com/powerswitch") SetAllSwitchesStateRequestDTO setAllSwitchesStateRequestDTO) throws PowerSwitchNotConfiguredFault {
        SwitchState state = SwitchState.valueOf(setAllSwitchesStateRequestDTO.getSwitchState().name());
        try {
            for (PowerSwitch item : switchService.getConfiguredPowerSwitches()) {
                switchService.setSwitchState(item.getId(), state);
            }
        } catch (SwitchNotFoundException e) {
            throw new IllegalStateException("Configuration error....");
        }
    }

    @Override
    public void setState(@WebParam(partName = "parameters", name = "setSwitchStateRequest", targetNamespace = "http://service.iot.com/powerswitch") SetSwitchStateRequestDTO setSwitchStateRequestDTO) throws PowerSwitchNotConfiguredFault {
        try {
            switchService.setSwitchState(setSwitchStateRequestDTO.getId(), SwitchState.valueOf(setSwitchStateRequestDTO.getStatus().name()));
        } catch (SwitchNotFoundException e) {
            throw new PowerSwitchNotConfiguredFault("Switch not existing...");
        }
    }

    @Override
    public GetPowerSwitchByIdResponseDTO getPowerSwitchById(@WebParam(partName = "parameters", name = "getPowerSwitchByIdRequest", targetNamespace = "http://service.iot.com/powerswitch") GetPowerSwitchByIdRequestDTO getPowerSwitchByIdRequestDTO) throws PowerSwitchNotConfiguredFault {
        GetPowerSwitchByIdResponseDTO dto = null;
        try {
            dto = new GetPowerSwitchByIdResponseDTO();
            PowerSwitch powerSwitch = switchService.getPowerSwitch(getPowerSwitchByIdRequestDTO.getId());
            dto.setSwitchType(buildSwitchTypeDto(powerSwitch));
        } catch (SwitchNotFoundException e) {
            throw new PowerSwitchNotConfiguredFault("Switch not existing...");
        }
        return dto;
    }

    private PowerSwitchType buildSwitchTypeDto(PowerSwitch source) throws PowerSwitchNotConfiguredFault {
        SwitchTypeBuilder builder = new SwitchTypeBuilder();
        builder.withId(source.getId())
                .withName(source.getName())
                .withSwitchState(com.iot.raspberry.remote.control.service.soap.spec.dto.SwitchState.fromValue(source.getState().name()));
        return builder.build();
    }

}
