package com.example.project.contract.spring

import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.role.IUserRoleRepository
import com.example.project.repository.user.IUserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
@Component
class RepositoryBeans(
        var userRepo: IUserRepository,
        var personRepo: IPersonRepository,
        var userRoleRepo: IUserRoleRepository
)