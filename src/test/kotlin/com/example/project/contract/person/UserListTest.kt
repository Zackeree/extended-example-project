package com.example.project.contract.person

import com.example.project.contract.responder.ListResponder
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
class UserListTest {

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

    val user = User("username", "email@address.com", "password")

    @Before
    fun init() {
        // Save a user object
        userRepo.save(user)

        // Persist one person object
        personRepo.save(Person("Cody", "Spath", user))

        // Persist another
        personRepo.save(Person("Derek", "McClellan", user))
    }

    @Test
    fun userIdNotFound_Failure() {
        var executed = false
        // Instantiate a concrete responder
        val responder = object : ListResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: List<PersonInfo>) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                // Set executed to true and make sure the command failed
                // for the right reason
                executed = true
                assertTrue(e[ErrorTag.USER].isNotEmpty())
            }
        }
        UserList(
                userId = user.id + 1L,
                responder = responder,
                personRepo = personRepo,
                userRepo = userRepo
        ).execute()
        // Make sure the expected scenario occurred
        assertTrue(executed)
    }

    @Test
    fun success() {
        var executed = false
        // Instantiate a concrete responder
        val responder = object : ListResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: List<PersonInfo>) {
                // Mark executed as true and make sure the
                // list size is two
                executed = true
                assertEquals(t.size, 2)
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        UserList(
                userId = user.id,
                responder = responder,
                userRepo = userRepo,
                personRepo = personRepo
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }
}