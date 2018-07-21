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

// Any tests that requires saving and persisting entities needs
// each of the below annotations
@DataJpaTest
@Transactional
@RunWith(SpringRunner::class)
class CreateTest : BaseCRUDTest() {

    // Each Command object should have a test case that covers
    // one of every possible logic scenario in the execute method
    // Method names should explain to some degree the scenario they
    // are testing

    // Each Spring Repository must be declared an Autowired
    // lateinit var since spring creates the interfaces
    // during runtime. That way, the test class can wait to
    // populate the repository objects until they have been
    // instantiated by Spring
    @Autowired
    private lateinit var userRepo: IUserRepository

    @Autowired
    private lateinit var personRepo: IPersonRepository

    // Set up a base request with a bad id
    private val baseRequest = Create.Request(
            userId = -1,
            firstName = "Cody",
            lastName = "Spath"
    )

    // Instantiate a user object
    private val user = User("username", "email@address.com", "password123")

    @Before
    fun init() {
        // Persist the user object
        userRepo.save(user)
        assertEquals(userRepo.count(), 1)
    }

    @Test
    fun userIdDoesNotExist_Failure() {
        var executed = false
        // The base request already has an invalid id, so
        // we can just use it
        val request = baseRequest
        val (id, error) = Create(
            request = request,
            personRepo = personRepo,
            userRepo = userRepo
        ).execute()
        assertNull(id)
        assertNotNull(error)
        error!!
        assertTrue(error[ErrorTag.USER].isNotEmpty())
    }

    @Test
    fun firstNameBlank_Failure() {
        var executed = false
        // Copy the base request with the bad parameter
        val request = baseRequest.copy(firstName = "", userId = user.id)
        val (id, error) = Create(
            request = request,
            personRepo = personRepo,
            userRepo = userRepo
        ).execute()
        assertNull(id)
        assertNotNull(error)
        error!!
        assertTrue(error[ErrorTag.FIRST_NAME].isNotEmpty())
    }

    @Test
    fun firstNameTooLong_Failure() {
        val newFirstName = "Cody".repeat(15)
        // Copy the base request with the bad parameter
        val request = baseRequest.copy(firstName = newFirstName, userId = user.id)
        val (id, error) = Create(
            request = request,
            personRepo = personRepo,
            userRepo = userRepo
        ).execute()
        assertNull(id)
        assertNotNull(error)
        error!!
        assertTrue(error[ErrorTag.FIRST_NAME].isNotEmpty())
    }

    @Test
    fun lastNameBlank_Failure() {
        var executed = false
        // Copy the base request with the bad parameter
        val request = baseRequest.copy(userId = user.id, lastName = "")
        val (id, error) = Create(
            request = request,
            personRepo = personRepo,
            userRepo = userRepo
        ).execute()
        assertNull(id)
        assertNotNull(error)
        error!!
        assertTrue(error[ErrorTag.LAST_NAME].isNotEmpty())
    }

    @Test
    fun lastNameTooLong_Failure() {
        var executed = false
        val newLastName = "Spath".repeat(15)
        // Copy the base request with the bad parameter
        val request = baseRequest.copy(lastName = newLastName, userId = user.id)
        val (id, e) = Create(
            request = request,
            personRepo = personRepo,
            userRepo = userRepo
        ).execute()
        assertNull(id)
        assertNotNull(e)
        e!!
        assertTrue(e[ErrorTag.LAST_NAME].isNotEmpty())
    }

    @Test
    fun create_Success() {
        var executed = false
        // Copy the base request with the correct user id
        val request = baseRequest.copy(userId = user.id)
        val (id, e) = Create(
            request = request,
            personRepo = personRepo,
            userRepo = userRepo
        ).execute()
        assertNull(e)
        assertNotNull(id)
        assertTrue(id!! >= 0)
    }
}