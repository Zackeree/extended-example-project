package com.example.project.controller.security

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LogoutHandlerImpl : LogoutHandler {
    override fun logout(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {

    }
}