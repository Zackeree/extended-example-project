package com.example.project.contract.user

import com.example.project.contract.responder.CreateResponder
import com.example.project.contract.responder.DeleteResponder
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.contract.responder.UpdateResponder
import com.example.project.repository.role.IUserRoleRepository
import com.example.project.repository.user.IUserRepository
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
@RunWith(SpringRunner::class)
class BaseUserFactoryTest {

    @Autowired
    private lateinit var userRepo: IUserRepository

    @Autowired
    private lateinit var userRoleRepo: IUserRoleRepository

    lateinit var factory: BaseUserFactory

    @Before
    fun init() {
        factory = BaseUserFactory(userRepo, userRoleRepo)
    }

    @Test
    fun retrieveId() {
        val cmd = factory.retrieve(
                id = 0,
                responder = object : RetrieveResponder<UserInfo, ErrorTag> {
                    override fun onSuccess(t: UserInfo) { }
                    override fun onFailure(e: Multimap<ErrorTag, String>) { }
                }
        )
        assertTrue(cmd is Retrieve)
    }

    @Test
    fun retrieveUserNameOrEmailAndPassword() {
        val cmd = factory.retrieve(
                request = FindByUsernameOrEmailAndPassword.Request(
                        usernameOrEmail = "username",
                        password = "password"
                ),
                responder = object : RetrieveResponder<UserInfo, ErrorTag> {
                    override fun onSuccess(t: UserInfo) { }
                    override fun onFailure(e: Multimap<ErrorTag, String>) { }
                }
        )
        assertTrue(cmd is FindByUsernameOrEmailAndPassword)
    }

    @Test
    fun create() {
        val cmd = factory.create(
                request = Create.Request(
                        username = "username",
                        email = "email@address.com",
                        password = "password123",
                        passwordConfirm = "password123"
                ),
                responder = object : CreateResponder<ErrorTag> {
                    override fun onSuccess(t: Long) { }
                    override fun onFailure(e: Multimap<ErrorTag, String>) { }
                }
        )
        assertTrue(cmd is Create)
    }

    @Test
    fun update() {
        val cmd = factory.update(
                request = Update.Request(
                        id = 0,
                        username = "newUsername",
                        email = "email@address.com"
                ),
                responder = object : UpdateResponder<ErrorTag> {
                    override fun onSuccess(t: Long) { }
                    override fun onFailure(e: Multimap<ErrorTag, String>) { }
                }
        )
        assertTrue(cmd is Update)
    }

    @Test
    fun delete() {
        val cmd = factory.delete(
                id = 1,
                responder = object : DeleteResponder<ErrorTag> {
                    override fun onSuccess(t: Long) { }
                    override fun onFailure(e: HashMultimap<ErrorTag, String>) { }
                }
        )
        assertTrue(cmd is Delete)
    }
}