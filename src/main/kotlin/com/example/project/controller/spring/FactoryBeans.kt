package com.example.project.controller.spring

import com.example.project.contract.person.BasePersonFactory
import com.example.project.contract.person.UserPersonWrapper
import com.example.project.controller.security.UserContextImpl
import com.example.project.contract.user.BaseUserFactory
import com.example.project.contract.user.UserUserWrapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FactoryBeans(
        private var repositories: RepositoryBeans
) : FactoryProvider {
    private val userContext = UserContextImpl(repositories.userRepo)

    @Bean
    override fun getUserWrapper(): UserUserWrapper {
        return UserUserWrapper(
                context = userContext,
                factory = BaseUserFactory(
                        userRepo = repositories.userRepo,
                        userRoleRepo = repositories.userRoleRepo
                ),
                userRepo = repositories.userRepo
        )
    }

    @Bean
    override fun getPersonWrapper(): UserPersonWrapper {
        return UserPersonWrapper(
                context = userContext,
                factory = BasePersonFactory(
                        userRepo = repositories.userRepo,
                        personRepo = repositories.personRepo
                ),
                userRepo = repositories.userRepo,
                personRepo = repositories.personRepo
        )
    }
}