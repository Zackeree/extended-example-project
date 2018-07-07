package com.example.project.controller.security

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

//@EnableWebSecurity
//class SecurityConfig(
//        private var authenticationProvider: AuthenticationProvider
//) : WebSecurityConfigurerAdapter {
//
//    override fun configure(auth: AuthenticationManagerBuilder?) {
//        auth?.authenticationProvider(authenticationProvider)
//    }
//}