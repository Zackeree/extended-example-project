package com.example.project.contract.person

import com.example.project.contract.BaseCRUDTest
import com.example.project.contract.responder.PageResponder
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.person.Person
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
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

// Any tests that requires saving and persisting entities needs
// each of the below annotations
@DataJpaTest
@Transactional
@RunWith(SpringRunner::class)
class FindByFirstNameTest : BaseCRUDTest() {

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

    // Set up a user object
    private val user = User("username", "email@address.com", "password123")

    @Before
    fun init() {
        // Save the user object
        userRepo.save(user)

        // Create and save one person object
        val person1 = Person("Cody", "Spath")
        person1.user = user
        personRepo.save(person1)

        //Create and save another person object
        val person2 = Person("Cody", "Spat")
        person2.user = user
        personRepo.save(person2)
    }

    @Test
    fun testValidConstraints_Success() {
        var executed = false
        // Declare our search parameter
        val firstName = "Cody"
        // Declare a pageable object
        val pageable = PageRequest.of(0, 5)
        // Instantiate a concrete responder
        val responder = object : PageResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: Page<PersonInfo>) {
                // Mark executed as true. We should also make
                // sure the size of the page object is
                executed = true
                assertTrue(t.content.size == 2)
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        // Execute the command
        FindByFirstName(
                firstName = firstName,
                pageable = pageable,
                responder = responder,
                personRepo = personRepo
        ).execute()
        // Make sure the expected scenario occurred
        assertTrue(executed)
    }

    @Test
    fun testBlankFirstName_Failure() {
        var executed = false
        // Declare our bad search parameter
        val firstName = ""
        // Declare the page request
        val pageable = PageRequest.of(0,5)
        // Instantiate a concrete responder
        val responder = object : PageResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: Page<PersonInfo>) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                // Mark executed as true. We should also check
                // to make sure it failed for the reason we
                // thought it did (empty first name)
                executed = true
                assertTrue(e[ErrorTag.FIRST_NAME].isNotEmpty())
            }
        }
        // Execute the command
        FindByFirstName(
                firstName = firstName,
                pageable = pageable,
                responder = responder,
                personRepo = personRepo
        ).execute()
        // Make sure the correct scenario happened
        assertTrue(executed)
    }

    @Test
    fun testFirstNameTooLong_Failure() {
        var executed = false
        // Declare our bad search parameter
        val firstName = "hello".repeat(20)
        // Declare the page request
        val pageable = PageRequest.of(0, 5)
        // Instantiate a concrete responder
        val responder = object : PageResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: Page<PersonInfo>) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                // Mark execute as true. Also make sure the command
                // failed for the reason we thought it was
                executed = true
                assertTrue(e[ErrorTag.FIRST_NAME].isNotEmpty())
            }
        }
        // Execute the command
        FindByFirstName(
                firstName = firstName,
                pageable = pageable,
                responder = responder,
                personRepo = personRepo
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }
}