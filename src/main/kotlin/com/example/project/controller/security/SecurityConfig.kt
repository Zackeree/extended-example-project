package com.example.project.controller.security

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@EnableWebSecurity
class SecurityConfig(
        private var authenticationProvider: AuthenticationProvider
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        if (http != null) {
            http.cors().and()
                    .authorizeRequests()
                    .antMatchers("/login**").permitAll()

            http.cors().and()
                    .formLogin()
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .loginPage("/login").permitAll()
                    .loginProcessingUrl("/login")
                    .successHandler(LoginHandlerImpl())
                    .failureHandler(AuthenticationFailureHandler { request, response, exception ->
                        request.setAttribute("lastEnteredEmail", request.getParameter("email"))
                        response.sendRedirect("/login")
                    })

            http.cors().and()
                    .logout()
                    .addLogoutHandler(LogoutHandlerImpl())
                    .logoutSuccessUrl("/login")
                    .logoutRequestMatcher(AntPathRequestMatcher("/logout"))
        }

    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.authenticationProvider(authenticationProvider)
    }
}