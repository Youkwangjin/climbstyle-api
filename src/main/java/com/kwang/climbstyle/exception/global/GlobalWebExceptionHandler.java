package com.kwang.climbstyle.exception.global;

import com.kwang.climbstyle.exception.ClimbStyleException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(annotations = Controller.class)
public class GlobalWebExceptionHandler {

    @ExceptionHandler(ClimbStyleException.class)
    public String handleClimbStyleException(ClimbStyleException e, HttpServletRequest request) {
        log.error("ClimbStyleException: {}", e.getMessage(), e);

        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, e.getHttpStatus().value());
        request.setAttribute("code", e.getCode());
        request.setAttribute("message", e.getMessage());

        return "forward:/error";
    }
}
