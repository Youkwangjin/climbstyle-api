package com.kwang.climbstyle.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class GlobalErrorController implements ErrorController {

    private final WebErrorResponse webErrorResponse;

    @RequestMapping("/error")
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) == null) {
            return new ModelAndView("redirect:/");
        }
        return webErrorResponse.response(request, response);
    }
}
