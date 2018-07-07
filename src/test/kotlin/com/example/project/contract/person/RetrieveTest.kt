package com.example.project.contract.person

import com.example.project.contract.BaseCRUDTest
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.contract.user.ErrorTag
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.person.Person
import com.example.project.repository.user.IUserRepository
import com.example.project.repository.user.User
import com.google.common.collect.Multimap
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
class RetrieveTest : BaseCRUDTest() {

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
    private lateinit var personRepo: IPersonRepository

    @Autowired
    private lateinit var userRepo: IUserRepository

    // Will be replaced by person id
    private var validId = -1L

    @Before
    fun init() {
        // Save a user object
        val user = userRepo.save(User("username", "email@address.com", "password123"))
        // Save a person object
        val person = Person("Cody", "Spath")
        person.user = user
        personRepo.save(person)
        // Set the id
        validId = person.id
    }

    @Test
    fun retrieve_idNotFound_Failure() {
        var executed = false
        // Instantiate a concrete responder
        val responder = object : RetrieveResponder<PersonInfo> {
            override fun onSuccess(t: PersonInfo) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                // Mark executed as true
                executed = true
            }
        }
        // Execute the command
        Retrieve(
                id = 666L,
                responder = responder,
                personRepo = personRepo
        ).execute()
        // Make sure the corrected scenario occurred
        assertTrue(executed)
    }

    @Test
    fun retrieve_Success() {
        var executed = false
        // Instantiate a concrete responder
        val responder = object : RetrieveResponder<PersonInfo> {
            override fun onSuccess(t: PersonInfo) {
                // Mark executed as true and make sure the
                // id of the callback matches the id of the
                // persisted person
                executed = true
                assertEquals(validId, t.id)
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        // Execute the command
        Retrieve(
                id = validId,
                responder = responder,
                personRepo = personRepo
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }
}