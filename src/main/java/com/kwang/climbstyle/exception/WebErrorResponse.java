package com.kwang.climbstyle.exception;

import com.kwang.climbstyle.code.http.HttpErrorCode;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@Component
public class WebErrorResponse {

    public ModelAndView response(HttpServletRequest request, HttpServletResponse response) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) statusCode = response.getStatus();

        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(httpStatus);

        Object code = request.getAttribute("code");
        Object message = request.getAttribute("message");

        HttpErrorCode errorCode = getHttpErrorCode(httpStatus);

        mav.addObject("httpStatus", httpStatus);
        mav.addObject("code", code != null ? code : (errorCode != null ? errorCode.getCode() : String.valueOf(httpStatus.value())));
        mav.addObject("message", message != null ? message : (errorCode != null ? errorCode.getMessage() : httpStatus.getReasonPhrase()));

        return mav;
    }

    private HttpErrorCode getHttpErrorCode(HttpStatus httpStatus) {
        return Arrays.stream(HttpErrorCode.values())
                .filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElse(null);
    }
}