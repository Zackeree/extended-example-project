package com.example.project.contract.person

import com.example.project.contract.BaseCRUDTest
import com.example.project.contract.responder.UpdateResponder
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
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

// Any tests that requires saving and persisting entities needs
// each of the below annotations
@DataJpaTest
@Transactional
@RunWith(SpringRunner::class)
class UpdateTest : BaseCRUDTest() {

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

    // Declare a base update request with a bad id
    private var baseRequest = Update.Request(
            id = -1L,
            firstName = "Thom",
            lastName = "Yorke"
    )

    // To be replaced by the valid person id
    private var existingId = -1L

    @Before
    fun init() {
        // Save the user object
        val user = userRepo.save(User("username", "email@address.com", "password123"))
        // Save the person object
        val person = Person("Cody", "Spath")
        person.user = user
        personRepo.save(person)
        // Update the existing id
        existingId = person.id
    }

    @Test
    fun idNotFound_Failure() {
        var executed = false
        // Call the buildUpdateCommand method
        // with the request with the bad id
        buildUpdateCommand(
                request = baseRequest,
                onSuccess = {
                    fail("Should not happen")
                },
                onFailure = {
                    // Mark executed as true and make sure the
                    // command failed for the expected reason
                    // (id doesn't exist)
                    executed = true
                    assertTrue((it[ErrorTag.ID].isNotEmpty()))
                }
        ).execute()
        // Make sure the expected scenario occurred
        assertTrue(executed)
    }

    @Test
    fun blankFirstName_Failure() {
        var executed = false
        // Call the buildUpdateCommand method
        // with the request with the right id
        // and a da first name parameter
        buildUpdateCommand(
                request = baseRequest.copy(
                        id = existingId,
                        firstName = ""
                ),
                onSuccess = {
                    fail("Should not succeed")
                },
                onFailure = {
                    // Mark executed as true and make sure the
                    // command failed for the expected reason
                    // (first name blank)
                    executed = true
                    assertTrue(it[ErrorTag.FIRST_NAME].isNotEmpty())
                }
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun firstNameTooLong_Failure() {
        var executed = false
        // Declare a bad first name parameter
        val newFirstName = "Thom".repeat(15)
        // Call the buildUpdateCommand with the
        // bad first name parameter
        buildUpdateCommand(
                request = baseRequest.copy(
                        id = existingId,
                        firstName = newFirstName
                ),
                onSuccess = {
                    fail("Should not succeed")
                },
                onFailure = {
                    // Mark executed as true and make sure the
                    // command failed for the expected reason
                    // (first name too long)
                    executed = true
                    assertTrue(it[ErrorTag.FIRST_NAME].isNotEmpty())
                }
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun lastNameBlank_Failure() {
        var executed = false
        // Call the buildUpdateCommand method with
        // the bad last name parameter
        buildUpdateCommand(
                request = baseRequest.copy(
                        id = existingId,
                        lastName = ""
                ),
                onSuccess = {
                    fail("Should not succeed")
                },
                onFailure = {
                    // Mark executed as true and make sure the
                    // command failed for the expected reason
                    // (last name blank)
                    executed = true
                    assertTrue(it[ErrorTag.LAST_NAME].isNotEmpty())
                }
        ).execute()
        // Make sure the expected scenario occurred
        assertTrue(executed)
    }

    @Test
    fun lastNameTooLong_Failure() {
        var executed = false
        // Declare the too long last name
        val newLastName = "Yorke".repeat(15)
        // call the buildUpdateCommand method with
        // the bad last name parameter
        buildUpdateCommand(
                request = baseRequest.copy(
                        id = existingId,
                        lastName = newLastName
                ),
                onSuccess = {
                    fail("Should not succeed")
                },
                onFailure = {
                    // Mark executed as true and make sure the
                    // command failed for the expected reason
                    // (last name too long)
                    executed = true
                    assertTrue(it[ErrorTag.LAST_NAME].isNotEmpty())
                }
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    /**
     * private method to build the update command. Pretty neat. Should've done
     * this for the other tests...
     */
    private fun buildUpdateCommand(request: Update.Request, onSuccess: (id: Long)->Unit, onFailure: (Multimap<ErrorTag, String>)->Unit): Update {
        val responder = object : UpdateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                onSuccess(t)
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                onFailure(e)
            }
        }

        return Update(request, responder, personRepo)
    }
}