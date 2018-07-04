package com.example.project.contract.spring

import com.example.project.contract.person.BasePersonFactory
import com.example.project.contract.person.UserPersonWrapper
import com.example.project.contract.security.UserContextImpl
import com.example.project.contract.user.BaseUserFactory
import com.example.project.contract.user.UserUserWrapper

class FactoryBeans : FactoryProvider {
    private val repositories = RepositoryBeans()
    private val userContext = UserContextImpl()

    override fun getUser(): UserUserWrapper {
        return UserUserWrapper(
                context = userContext,
                factory = BaseUserFactory(
                        userRepo = repositories.userRepo,
                        userRoleRepo = repositories.userRoleRepo
                ),
                userRepo = repositories.userRepo
        )
    }

    override fun getPerson(): UserPersonWrapper {
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