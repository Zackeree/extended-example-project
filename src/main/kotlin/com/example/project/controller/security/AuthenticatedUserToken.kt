package com.example.project.controller.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

/**
 * User Token that is created by spring security upon login
 */
class AuthenticatedUserToken(
        emailOrUsername: String,
        password: String,
        authorities: Collection<GrantedAuthority>,
        val userId: Long?
) : UsernamePasswordAuthenticationToken(emailOrUsername, password, authorities)

