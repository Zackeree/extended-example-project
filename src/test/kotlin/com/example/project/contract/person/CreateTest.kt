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
        // Instantiate a concrete responder
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                // Mark executed as true since the scenario expectedly
                // failed. We should also check to make sure it failed
                // for the reason we thought it did (invalid user id)
                executed = true
                assertTrue(e[ErrorTag.USER].isNotEmpty())
            }
        }
        // Execute the create command
        Create(
                request = request,
                responder = responder,
                personRepo = personRepo,
                userRepo = userRepo
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun firstNameBlank_Failure() {
        var executed = false
        // Copy the base request with the bad parameter
        val request = baseRequest.copy(firstName = "", userId = user.id)
        // Instantiate a concrete responder
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                // Mark executed as true since the scenario expectedly
                // failed. We should also check to make sure it failed
                // for the reason we thought it did (first name blank)
                executed = true
                assertTrue(e[ErrorTag.FIRST_NAME].isNotEmpty())
            }
        }
        // Execute the command
        Create(
                request = request,
                responder = responder,
                personRepo = personRepo,
                userRepo = userRepo
        ).execute()
        // Make sure the correct scenario happened
        assertTrue(executed)
    }

    @Test
    fun firstNameTooLong_Failure() {
        var executed = false
        val newFirstName = "Cody".repeat(15)
        // Copy the base request with the bad parameter
        val request = baseRequest.copy(firstName = newFirstName, userId = user.id)
        // Instantiate a concrete responder
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                // Mark executed as true since the scenario expectedly
                // failed. We should also check to make sure it failed
                // for the reason we thought it did (first name too long)
                executed = true
                assertTrue(e[ErrorTag.FIRST_NAME].isNotEmpty())
            }
        }
        // Execute the command
        Create(
                request = request,
                responder = responder,
                personRepo = personRepo,
                userRepo = userRepo
        ).execute()

        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun lastNameBlank_Failure() {
        var executed = false
        // Copy the base request with the bad parameter
        val request = baseRequest.copy(userId = user.id, lastName = "")
        // Instantiate a concrete responder
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                // Mark executed as true since the scenario expectedly
                // failed. We should also check to make sure it failed
                // for the reason we thought it did (last name blank)
                executed = true
                assertTrue(e[ErrorTag.LAST_NAME].isNotEmpty())
            }
        }
        // Execute the command
        Create(
                request = request,
                responder = responder,
                personRepo = personRepo,
                userRepo = userRepo
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun lastNameTooLong_Failure() {
        var executed = false
        val newLastName = "Spath".repeat(15)
        // Copy the base request with the bad parameter
        val request = baseRequest.copy(lastName = newLastName, userId = user.id)
        // Instantiate a concrete responder
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                // Mark executed as true since the scenario expectedly
                // failed. We should also check to make sure it failed
                // for the reason we thought it did (last name too long)

                executed = true
                assertTrue(e[ErrorTag.LAST_NAME].isNotEmpty())
            }
        }
        // Execute the command
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
        // Copy the base request with the correct user id
        val request = baseRequest.copy(userId = user.id)
        // Instantiate a concrete responder
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                // Mark executed true
                executed = true
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        // Execute the command
        Create(
                request = request,
                responder = responder,
                personRepo = personRepo,
                userRepo = userRepo
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }
}