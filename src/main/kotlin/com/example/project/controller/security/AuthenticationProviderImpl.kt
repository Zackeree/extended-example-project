package com.example.project.controller.security

import com.example.project.contract.responder.RetrieveResponder
import com.example.project.contract.user.ErrorTag
import com.example.project.contract.user.Validate
import com.example.project.contract.user.UserInfo
import com.example.project.service.security.UserDetailsImpl
import com.example.project.repository.user.IUserRepository
import com.google.common.collect.Multimap
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class AuthenticationProviderImpl(
        private var userDetailsService: UserDetailsService,
        private var userRepo: IUserRepository
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication {
        val context = SecurityContextHolder.getContext()

        if (context.authentication != null && authentication?.name == context.authentication.name)
            return context.authentication

        val userDetailsImpl = userDetailsService.loadUserByUsername(authentication?.name) as UserDetailsImpl

        val verified = execute(
                username = userDetailsImpl.username,
                password = userDetailsImpl.password
        )

        if (!verified)
            throw AuthenticationCredentialsNotFoundException("Invalid Email or Password")

        return AuthenticatedUserToken(
                userId = userDetailsImpl.id,
                authorities = userDetailsImpl.authorities,
                password = userDetailsImpl.password,
                emailOrUsername = userDetailsImpl.username

        )
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    private fun execute(username: String, password: String): Boolean {
        var verified = false
        Validate(
                request = Validate.Request(
                        usernameOrEmail = username,
                        password = password
                ),
                responder = object : RetrieveResponder<UserInfo, ErrorTag> {
                    override fun onSuccess(t: UserInfo) {
                        verified = true
                    }

                    override fun onFailure(e: Multimap<ErrorTag, String>) {
                        verified = false
                    }
                },
                userRepo = userRepo


        ).execute()

        return verified
    }
}