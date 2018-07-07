package com.example.project.service.security

import com.example.project.repository.user.IUserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
        var userRepo: IUserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw UsernameNotFoundException("Invalid username or email")
        }

        val user = userRepo.findByUsernameOrEmail(username) ?: throw UsernameNotFoundException("Invalid username or email")

        if (user.roles.isEmpty()) {
            throw UsernameNotFoundException("This User does not have any roles")
        }

        var grantedAuths: Set<GrantedAuthority> = HashSet()

        user.roles.forEach {
            grantedAuths = grantedAuths.plus(SimpleGrantedAuthority("ROLE_${it.role}"))
        }

        return UserDetailsImpl(user, grantedAuths)

    }
}