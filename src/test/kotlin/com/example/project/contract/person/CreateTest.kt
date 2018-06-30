package com.example.project.contract.person

import com.example.project.contract.BaseCRUDTest
import com.example.project.contract.responder.CreateResponder
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.user.IUserRepository
import com.example.project.repository.user.User
import com.google.common.collect.Multimap
import org.junit.After
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
class CreateTest : BaseCRUDTest() {

    @Autowired
    private lateinit var userRepo: IUserRepository

    @Autowired
    private lateinit var personRepo: IPersonRepository

    private val baseRequest = Create.Request(
            userId = -1,
            firstName = "Cody",
            lastName = "Spath"
    )

    private val user = User("username", "email@address.com", "password123")

    @Before
    fun init() {
        userRepo.save(user)
        assertEquals(userRepo.count(), 1)
    }

    @Test
    fun userIdDoesNotExist_Failure() {
        var executed = false
        val request = baseRequest
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
                assertTrue(e[ErrorTag.USER].isNotEmpty())
            }
        }
        Create(
                request = request,
                responder = responder,
                personRepo = personRepo,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun firstNameBlank_Failure() {
        var executed = false
        val request = baseRequest.copy(firstName = "", userId = user.id)
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
                assertTrue(e[ErrorTag.FIRST_NAME].isNotEmpty())
            }
        }
        Create(
                request = request,
                responder = responder,
                personRepo = personRepo,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun firstNameTooLong_Failure() {
        var executed = false
        val newFirstName = "Cody".repeat(15)
        val request = baseRequest.copy(firstName = newFirstName, userId = user.id)
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
                assertTrue(e[ErrorTag.FIRST_NAME].isNotEmpty())
            }
        }
        Create(
                request = request,
                responder = responder,
                personRepo = personRepo,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun lastNameBlank_Failure() {
        var executed = false
        val request = baseRequest.copy(userId = user.id, lastName = "")
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
                assertTrue(e[ErrorTag.LAST_NAME].isNotEmpty())
            }
        }
        Create(
                request = request,
                responder = responder,
                personRepo = personRepo,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun lastNameTooLong_Failure() {
        var executed = false
        val newLastName = "Spath".repeat(15)
        val request = baseRequest.copy(lastName = newLastName, userId = user.id)
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
                assertTrue(e[ErrorTag.LAST_NAME].isNotEmpty())
            }
        }
        Create(
                request = request,
                responder = responder,
                personRepo = personRepo,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun create_Success() {
        var executed = false
        val request = baseRequest.copy(userId = user.id)
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                executed = true
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        Create(
                request = request,
                responder = responder,
                personRepo = personRepo,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }
}