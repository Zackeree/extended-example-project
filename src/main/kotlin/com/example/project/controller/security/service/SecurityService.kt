package com.example.project.controller.security.service

import com.example.project.contract.Command
import com.example.project.controller.security.AuthenticatedUserToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Service
class SecurityService(
        private var authenticationManager: AuthenticationManager,
        private var email: String,
        private var password: String,
        private var request: HttpServletRequest
) : Command {
    override fun execute() {
        val authenticationToken: Authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, password))

        if (authenticationToken is AuthenticatedUserToken) {
            if (authenticationToken.isAuthenticated) {
                val securityContext: SecurityContext = SecurityContextHolder.getContext()

                securityContext.authentication = authenticationToken

                val session: HttpSession = request.session

                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext)

                //10 days
                session.maxInactiveInterval = 60 * 60 * 24 * 10
            }
        }
    }
}