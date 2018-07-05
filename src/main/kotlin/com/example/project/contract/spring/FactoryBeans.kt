package com.example.project.contract.spring

import com.example.project.contract.person.BasePersonFactory
import com.example.project.contract.person.UserPersonWrapper
import com.example.project.contract.security.UserContextImpl
import com.example.project.contract.user.BaseUserFactory
import com.example.project.contract.user.UserUserWrapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
class FactoryBeans : FactoryProvider {
    @Autowired
    private val repositories = RepositoryBeans()

    private val userContext = UserContextImpl()

    @Bean
    override fun getUserWrapper(): UserUserWrapper {
        return UserUserWrapper(
                context = userContext,
                factory = BaseUserFactory(
                        userRepo = repositories.userRepo(),
                        userRoleRepo = repositories.userRoleRepo()
                ),
                userRepo = repositories.userRepo()
        )
    }

    @Bean
    override fun getPersonWrapper(): UserPersonWrapper {
        return UserPersonWrapper(
                context = userContext,
                factory = BasePersonFactory(
                        userRepo = repositories.userRepo(),
                        personRepo = repositories.personRepo()
                ),
                userRepo = repositories.userRepo(),
                personRepo = repositories.personRepo()
        )
    }
}