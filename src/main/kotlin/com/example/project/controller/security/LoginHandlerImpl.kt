package com.example.project.controller.security

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginHandlerImpl : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
        //10 days
        request?.session?.maxInactiveInterval = 60 * 60 * 24 * 10
    }
}