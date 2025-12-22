package com.kwang.climbstyle.exception.global;

import com.kwang.climbstyle.exception.ClimbStyleException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(annotations = Controller.class)
public class GlobalWebExceptionHandler {

    @ExceptionHandler(ClimbStyleException.class)
    public String handleClimbStyleException(ClimbStyleException e, HttpServletRequest request) {
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, e.getHttpStatus().value());
        request.setAttribute("code", e.getCode());
        request.setAttribute("message", e.getMessage());

        return "forward:/error";
    }
}
