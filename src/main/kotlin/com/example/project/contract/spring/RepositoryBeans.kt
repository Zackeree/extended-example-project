package com.example.project.contract.spring

import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.role.IUserRoleRepository
import com.example.project.repository.user.IUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoryBeans {
    @Autowired
    lateinit var userRepo: IUserRepository
    @Autowired
    lateinit var personRepo: IPersonRepository
    @Autowired
    lateinit var userRoleRepo: IUserRoleRepository

    @Bean
    fun userRepo(): IUserRepository {
        return userRepo
    }

    @Bean
    fun personRepo(): IPersonRepository {
        return personRepo
    }

    @Bean
    fun userRoleRepo(): IUserRoleRepository {
        return userRoleRepo
    }
}