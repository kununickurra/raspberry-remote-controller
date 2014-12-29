package com.iot.raspberry.ctrl.service.rest.impl.controller;

import com.iot.raspberry.ctrl.service.rest.impl.dto.ErrorDTO;
import com.iot.raspberry.ctrl.service.spec.exception.SwitchNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller Advice that handle {@link SwitchNotFoundException} and return an Http 400
 * with a {@link ErrorDTO}
 */
@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler({SwitchNotFoundException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleException(SwitchNotFoundException e) {
        return new ErrorDTO(1001, "PowerSwitch "+e.getSwitchId()+" not found...");
    }
}
