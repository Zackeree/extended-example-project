package com.example.project.contract.spring

import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.role.IUserRoleRepository
import com.example.project.repository.user.IUserRepository
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoryBeans {
    lateinit var userRepo: IUserRepository
    lateinit var personRepo: IPersonRepository
    lateinit var userRoleRepo: IUserRoleRepository
}