package com.example.project.service.security

import com.example.project.repository.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(
        user: User,
        private var grantedAuths: Set<GrantedAuthority>
) : UserDetails {
    var id = user.id
    private var username = user.username
    private var password = user.password

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonLocked(): Boolean {
        return false
    }

    override fun isAccountNonExpired(): Boolean {
        return false
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun getPassword(): String {
        return password
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return grantedAuths.toMutableList()
    }
}