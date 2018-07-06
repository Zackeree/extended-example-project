package com.example.project.contract.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class AuthenticatedUserToken(
        email: String,
        password: String,
        authorities: Collection<GrantedAuthority>,
        val userId: Long?
) : UsernamePasswordAuthenticationToken(email, password, authorities)

