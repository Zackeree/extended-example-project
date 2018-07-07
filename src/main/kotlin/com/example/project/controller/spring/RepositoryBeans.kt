package com.example.project.controller.spring

import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.role.IUserRoleRepository
import com.example.project.repository.user.IUserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
@Component
class RepositoryBeans(
        val userRepo: IUserRepository,
        val personRepo: IPersonRepository,
        val userRoleRepo: IUserRoleRepository
)