package com.example.project.contract.user

import com.example.project.contract.BaseUserWrapperTest
import com.example.project.contract.responder.CreateResponder
import com.example.project.contract.responder.DeleteResponder
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.contract.responder.UpdateResponder
import com.example.project.controller.security.FakeUserContext
import com.example.project.repository.role.IUserRoleRepository
import com.example.project.repository.user.IUserRepository
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@DataJpaTest
@SpringBootTest
@RunWith(SpringRunner::class)
class UserUserWrapperTest : BaseUserWrapperTest() {

    @Autowired
    private lateinit var userRepo: IUserRepository

    @Autowired
    private lateinit var userRoleRepo: IUserRoleRepository

    private val baseCreateRequest = Create.Request(
            username = "username",
            email = "email@address.com",
            password = "password123",
            passwordConfirm = "password123"
    )

    private val baseUpdateRequest = Update.Request(
            id = -1,
            username = "newUsername",
            email = "newEmail@address.com"
    )

    @Test
    fun create_AllRequirements_Success() {
        val context = FakeUserContext()
        val factory = BaseUserFactory(userRepo, userRoleRepo)
        val wrapper = UserUserWrapper(context, factory, userRepo)
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                executed = true
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail.")
            }
        }
        wrapper.factory(failure).create(
                request = baseCreateRequest,
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun retrieveId_AllRequirements_Success() {
        val context = FakeUserContext()
        val factory = BaseUserFactory(userRepo, userRoleRepo)
        val wrapper = UserUserWrapper(context, factory, userRepo)
        val user = userRepo.save(baseCreateRequest.toEntity())
        val responder = object : RetrieveResponder<UserInfo, ErrorTag> {
            override fun onSuccess(t: UserInfo) {
                executed = true
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        wrapper.factory(failure).retrieve(
                id = user.id,
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun update_AllRequirements_Success() {
        val context = FakeUserContext()
        val factory = BaseUserFactory(userRepo, userRoleRepo)
        val wrapper = UserUserWrapper(context, factory, userRepo)
        val user = userRepo.save(baseCreateRequest.toEntity())
        context.login(userId = user.id)
        val responder = object : UpdateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                executed = true
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        wrapper.factory(failure).update(
                request = baseUpdateRequest.copy(id = user.id),
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun update_NotLoggedIn_Failure() {
        val context = FakeUserContext()
        context.currentRoles = mutableListOf()
        val factory = BaseUserFactory(userRepo, userRoleRepo)
        val wrapper = UserUserWrapper(context, factory, userRepo)
        val user = userRepo.save(baseCreateRequest.toEntity())
        val responder = object : UpdateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        shouldFailOnPrecondition = true
        wrapper.factory(failure).update(
                request = baseUpdateRequest.copy(id = user.id),
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun delete_AllRequirements_Success() {
        val context = FakeUserContext()
        val factory = BaseUserFactory(userRepo, userRoleRepo)
        val wrapper = UserUserWrapper(context, factory, userRepo)
        val user = userRepo.save(baseCreateRequest.toEntity())
        context.login(userId = user.id)
        val responder = object : DeleteResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                executed = true
            }

            override fun onFailure(e: HashMultimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        wrapper.factory(failure).delete(
                id = user.id,
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun delete_NotLoggedIn_Failure() {
        val context = FakeUserContext()
        context.currentRoles = mutableListOf()
        val factory = BaseUserFactory(userRepo, userRoleRepo)
        val wrapper = UserUserWrapper(context, factory, userRepo)
        val user = userRepo.save(baseCreateRequest.toEntity())
        val responder = object : DeleteResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: HashMultimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        shouldFailOnPrecondition = true
        wrapper.factory(failure).delete(
                id = user.id,
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun retrieveUsernameOrEmailAndPassword_AllRequirements_Success() {
        val context = FakeUserContext()
        context.currentRoles = mutableListOf()
        userRepo.save(baseCreateRequest.toEntity())
        val factory = BaseUserFactory(userRepo, userRoleRepo)
        val wrapper = UserUserWrapper(context, factory, userRepo)
        val responder = object : RetrieveResponder<UserInfo, ErrorTag> {
            override fun onSuccess(t: UserInfo) {
                executed = true
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        wrapper.factory(failure).retrieve(
                request = Validate.Request(
                        usernameOrEmail = baseCreateRequest.username,
                        password = "password123"
                ),
                responder = responder
        ).execute()
        assertTrue(executed)
    }
}