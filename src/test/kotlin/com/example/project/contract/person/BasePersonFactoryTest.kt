package com.example.project.contract.person

import com.example.project.contract.responder.*
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.user.IUserRepository
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import org.junit.Assert.*
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
class BasePersonFactoryTest {

    // Each concrete implementation of the factory interface
    // should have a test case that ensures that each factory
    // method returns the expected command object

    // Each Spring Repository must be declared an Autowired
    // lateinit var since spring creates the interfaces
    // during runtime. That way, the test class can wait to
    // populate the repository objects until they have been
    // instantiated by Spring
    @Autowired
    private lateinit var userRepo: IUserRepository

    @Autowired
    private lateinit var personRepo: IPersonRepository

    @Test
    fun retrieve() {
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Call the factory method so it returns the command object
        val cmd = factory.retrieve(
                id = 0,
                responder = object : RetrieveResponder<PersonInfo> {
                    override fun onSuccess(t: PersonInfo) { }
                    override fun onFailure(e: Multimap<com.example.project.contract.user.ErrorTag, String>) { }
                }
        )
        // Make sure the command returned is a Retrieve command
        assertTrue(cmd is Retrieve)
    }

    @Test
    fun create() {
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Call the factory method so it returns the command object
        val cmd = factory.create(
                request = Create.Request(
                        userId = 0,
                        firstName = "Cody",
                        lastName = "Spath"
                ),
                responder = object : CreateResponder<ErrorTag> {
                    override fun onSuccess(t: Long) { }
                    override fun onFailure(e: Multimap<ErrorTag, String>) { }
                }
        )
        // Make sure the command returned is a Create command
        assertTrue(cmd is Create)
    }

    @Test
    fun update() {
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Call the factory method so it returns the command object
        val cmd = factory.update(
                request = Update.Request(
                        id = 0,
                        firstName = "Thom",
                        lastName = "Yorke"
                ),
                responder = object : UpdateResponder<ErrorTag> {
                    override fun onSuccess(t: Long) { }
                    override fun onFailure(e: Multimap<ErrorTag, String>) { }
                }
        )
        // Make sure the command returned is an Update command
        assertTrue(cmd is Update)
    }

    @Test
    fun delete() {
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Call the factory method so it returns the command object
        val cmd = factory.delete(
                id = 0,
                responder = object : DeleteResponder<ErrorTag> {
                    override fun onSuccess(t: Long) { }
                    override fun onFailure(e: HashMultimap<ErrorTag, String>) { }
                }
        )
        // Make sure the command returned is a Delete command
        assertTrue(cmd is Delete)
    }

    @Test
    fun firstName() {
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Call the factory method so it returns the command object
        val cmd = factory.findByFirstName(
                firstName = "Cody",
                pageable = PageRequest.of(0, 20),
                responder = object : PageResponder<PersonInfo, ErrorTag> {
                    override fun onSuccess(t: Page<PersonInfo>) { }
                    override fun onFailure(e: Multimap<ErrorTag, String>) { }
                }
        )
        // Make sure the command returned is a FindByFirstName command
        assertTrue(cmd is FindByFirstName)
    }

    @Test
    fun lastName() {
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Call the factory method so it returns the command object
        val cmd = factory.findByLastName(
                lastName = "Spath",
                pageable = PageRequest.of(0, 25),
                responder = object : PageResponder<PersonInfo, ErrorTag> {
                    override fun onSuccess(t: Page<PersonInfo>) { }
                    override fun onFailure(e: Multimap<ErrorTag, String>) { }
                }
        )
        // Make sure the command returned is a FindByLastName command
        assertTrue(cmd is FindByLastName)
    }
}