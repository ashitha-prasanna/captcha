package com.captcha.captchademo.Configuration;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CaptchaValidationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("/login".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
            String captcha = request.getParameter("captcha");
            String sessionCaptcha = (String) request.getSession().getAttribute("captcha");

            if (captcha == null || !captcha.equalsIgnoreCase(sessionCaptcha)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid captcha");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
