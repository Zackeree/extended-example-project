package com.example.project.contract.person

import com.example.project.contract.BaseCRUDTest
import com.example.project.contract.responder.DeleteResponder
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.person.Person
import com.example.project.repository.user.IUserRepository
import com.example.project.repository.user.User
import com.google.common.collect.HashMultimap
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

// Any tests that requires saving and persisting entities needs
// each of the below annotations
@DataJpaTest
@Transactional
@RunWith(SpringRunner::class)
class DeleteTest : BaseCRUDTest() {

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

    private var existingId = -1L

    @Before
    fun init() {
        // Persist a user object
        val user = userRepo.save(User("username", "email@address.com", "password123"))
        // Instantiate a person object and set
        val person = Person("Cody", "Spath")
        person.user = user
        // Save the person
        personRepo.save(person)
        existingId = person.id
    }

    @Test
    fun idDoesNotExist_Failure() {
        var executed = false
        // Instantiate a concrete responder
        val responder = object : DeleteResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: HashMultimap<ErrorTag, String>) {
                // Mark executed as true since the scenario expectedly
                // failed.
                executed = true
            }
        }
        // Execute the delete command
        Delete(
                id = 666L,
                responder = responder,
                personRepo = personRepo
        ).execute()
        // Make sure the correct scenario occurred
        assertEquals(personRepo.count(), 1)
        assertTrue(executed)
    }

    @Test
    fun delete_Success() {
        var executed = false
        // Instantiate a concrete responder
        val responder = object : DeleteResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                // Mark executed as true since the scenario passed
                // We should also check to make sure the returned id
                // is the correct one
                executed = true
                assertEquals(existingId, t)
            }

            override fun onFailure(e: HashMultimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        // Execute the command
        Delete(
                id = existingId,
                responder = responder,
                personRepo = personRepo
        ).execute()
        // Make sure the correct scenario occurred and the
        // person was actually deleted
        assertEquals(personRepo.count(), 0)
        assertTrue(executed)
    }

}