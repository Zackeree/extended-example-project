package com.example.project.contract.person

import com.example.project.contract.BaseUserWrapperTest
import com.example.project.contract.responder.*
import com.example.project.controller.security.FakeUserContext
import com.example.project.controller.security.UserRole
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.person.Person
import com.example.project.repository.user.IUserRepository
import com.example.project.repository.user.User
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit4.SpringRunner

// Any tests that requires saving and persisting entities needs
// each of the below annotations
@DataJpaTest
@SpringBootTest
@RunWith(SpringRunner::class)
class UserPersonWrapperTest : BaseUserWrapperTest() {

    // Each wrapper factory test should check all of the different
    // scenarios that can cause success and failure (missing roles,
    // not logged in, etc) for each factory method

    // Each Spring Repository must be declared an Autowired
    // lateinit var since spring creates the interfaces
    // during runtime. That way, the test class can wait to
    // populate the repository objects until they have been
    // instantiated by Spring
    @Autowired
    private lateinit var userRepo: IUserRepository

    @Autowired
    private lateinit var personRepo: IPersonRepository

    private val baseCreateRequest = Create.Request(
            userId = -1L,
            firstName = "Cody",
            lastName = "Spath"
    )

    private val baseUpdateRequest = Update.Request(
            id = -1L,
            firstName = "Nigel",
            lastName = "Godrich"
    )

    // To be replaced when user is saved
    private var existingUserId = -1L

    // To be replaced when person is saved
    private var existingPersonId = -1L

    private val context = FakeUserContext()

    @Before
    fun init() {
        // Save a user
        val user = User("username", "email@address.com", "password123")
        userRepo.save(user)

        // Set the existing user id
        existingUserId = user.id

        // Save a person
        val person = Person("Thom", "Yorke")
        person.user = user
        personRepo.save(person)

        // Set the existing person id
        existingPersonId = person.id
    }

    @Test
    fun create_AllRequirements_Success() {
        // Update our context to have a valid user id
        context.login(existingUserId)
        // Instantiate the factory
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate the wrapper
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Instantiate a concrete responder object
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                executed = true
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        // Call the wrapper factory method and execute the command
        wrapper.factory(failure).create(
                request = baseCreateRequest.copy(userId = existingUserId),
                responder = responder
        ).execute()
        // Make sure the expected scenario occurred
        assertTrue(executed)
    }

    @Test
    fun create_UserNotOwner_Failure() {
        // Update our context to have a user id that is not the owner's
        context.login(existingUserId + 1L)
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate the wrapper object
        val wrapper  = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Instantiate a concrete responder
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        // This should fail on precondition, so mark the flag
        shouldFailOnPrecondition = true
        // Call the wrapper factory method and execute the command
        wrapper.factory(failure).create(
                request = baseCreateRequest.copy(userId = existingUserId),
                responder = responder
        ).execute()
        // Make sure the expected scenario occurred
        assertTrue(executed)
    }

    @Test
    fun create_NotLoggedIn_Failure() {
        // Set the list of roles to an empty list
        context.currentRoles = mutableListOf()
        // Instantiate a factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate a wrapper object
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Instantiate a concrete responder
        val responder = object : CreateResponder<ErrorTag> {
            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }

            override fun onSuccess(t: Long) {
                fail("Should fail on precondition")
            }
        }
        // This should fail on precondition, so set the flag
        shouldFailOnPrecondition = true
        // Call the wrapper factory method and execute the command
        wrapper.factory(failure).create(
                request = baseCreateRequest.copy(userId = existingUserId),
                responder = responder
        ).execute()
        // Make sure the expected scenario occurred
        assertTrue(executed)
    }

    @Test
    fun update_AllRequirements_Success() {
        // Update the user context to have a valid user id
        context.login(existingUserId)
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate the wrapper object
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Instantiate a concrete responder
        val responder = object : UpdateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                // Mark executed as true
                executed = true
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        // call the wrapper factory
        wrapper.factory(failure).update(
                request = baseUpdateRequest.copy(id = existingUserId),
                responder = responder
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun update_UserNotLoggedIn_Failure() {
        // Update the user context to have an empty list of roles
        context.currentRoles = mutableListOf()
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate the wrapper object
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Instantiate a concrete update
        val responder = object : UpdateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        // This should fail on precondition, so set the flag
        shouldFailOnPrecondition = true
        // Call the wrapper factory method and execute the command
        wrapper.factory(failure).update(
                request = baseUpdateRequest.copy(id = existingPersonId),
                responder = responder
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun update_UserNotOwner_Failure() {
        // update the context to have a user id that isn't the owner's
        context.login(existingUserId + 1L)
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate the wrapper object
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Instantiate a concrete responder
        val responder = object : UpdateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        // This should fail on precondition, so set the flag
        shouldFailOnPrecondition = true
        // Call the wrapper factory method and execute the command
        wrapper.factory(failure).update(
                request = baseUpdateRequest.copy(id = existingUserId),
                responder = responder
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun retrieve_AllRequirements_Success() {
        // Update the user context to have a valid user id
        context.login(existingUserId)
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate the wrapper object
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Instantiate a concrete retrieve responder
        val responder = object : RetrieveResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: PersonInfo) {
                executed = true
                assertEquals(t.id, existingPersonId)
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should")
            }
        }
        // Call the wrapper factory method and execute the command
        wrapper.factory(failure).retrieve(
                id = existingPersonId,
                responder = responder
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun retrieve_NotLoggedIn_Failure() {
        // Update the context to have an empty list of roles
        context.currentRoles = mutableListOf()
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate the wrapper object
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Instantiate a concrete responder
        val responder = object : RetrieveResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: PersonInfo) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }

        }
        // This should fail on precondition, so mark the flag
        shouldFailOnPrecondition = true
        // Call the wrapper factory method and execute the command
        wrapper.factory(failure).retrieve(
                id = existingPersonId,
                responder = responder
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun retrieve_UserNotOwner_Failure() {
        // Create a new user
        val newUser = userRepo.save(User("newUser", "email@gmail.com", "password"))
        // Set the context id to the new user id
        context.login(newUser.id)
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate the wrapper object
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Instantiate a concrete responder
        val responder = object : RetrieveResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: PersonInfo) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        // This should fail on precondition, so set the flag
        shouldFailOnPrecondition = true
        // Call the wrapper factory method and execute the command
        wrapper.factory(failure).retrieve(
                id = existingPersonId,
                responder = responder
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun delete_AllRequirements_Success() {
        // Update the context to have the valid user id
        context.login(existingUserId)
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate the wrapper object
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Instantiate a concrete responder
        val responder = object : DeleteResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                // Mark executed as true and make sure the id
                // is the existing person id
                executed = true
                assertEquals(t, existingPersonId)
            }

            override fun onFailure(e: HashMultimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        // Call the wrapper factory method and execute the command
        wrapper.factory(failure).delete(
                id = existingPersonId,
                responder = responder
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun delete_NotLoggedIn_Failure() {
        // Update the context to have an empty list of roles
        context.currentRoles = mutableListOf()
        // Instantiate a factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate a wrapper object
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Instantiate a concrete responder
        val responder = object : DeleteResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: HashMultimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        // This should fail on precondition, so mark the flag
        shouldFailOnPrecondition = true
        // Call the wrapper factory method and execute the command
        wrapper.factory(failure).delete(
                id = existingPersonId,
                responder = responder
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun delete_UserNotOwner_Failure() {
        // Update the context to have an id that isn't the owner's
        context.login(existingUserId + 1)
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate the wrapper object
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Instantiate a concrete responder
        val responder = object : DeleteResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: HashMultimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        // This should fail on precondition, so mark the flag
        shouldFailOnPrecondition = true
        // Call the wrapper factory method and execute the command
        wrapper.factory(failure).delete(
                id = existingPersonId,
                responder = responder
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun userList_AllConstraints_Success() {
        // Update the context to have an id that is valid
        context.login(existingUserId)
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate the wrapper object
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Instantiate a concrete responder
        val responder = object : ListResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: List<PersonInfo>) {
                executed = true
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        // Call the wrapper factory method and execute the command
        wrapper.factory(failure).list(
                userId = existingUserId,
                responder = responder
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun userList_UserNotOwner_Failure() {
        // Login the user to an id that is not the owners
        context.login(existingUserId + 1L)
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate the wrapper object
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Instantiate a concrete responder
        val responder = object : ListResponder<PersonInfo, ErrorTag> {
            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }

            override fun onSuccess(t: List<PersonInfo>) {
                fail("Should fail on precondition")
            }
        }
        // This should fail on precondition, so mark the flag
        shouldFailOnPrecondition = true
        // Call the wrapper factory method and execute the command
        wrapper.factory(failure).list(
                userId = context.currentUserId()!!,
                responder = responder
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun findByFirstName_Admin_Success() {
        // Update the context to have a role of ADMIN
        context.currentRoles = mutableListOf(UserRole.ADMIN)
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate the wrapper object
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Declare the search parameter
        val firstName = "Cody"
        // Declare the pageable object
        val pageable = PageRequest.of(0, 5)
        // Instantiate a concrete responder
        val responder = object : PageResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: Page<PersonInfo>) {
                // Mark executed as true
                executed = true
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        // Call the wrapper factory method and execute the command
        wrapper.factory(failure).findByFirstName(
                firstName = firstName,
                pageable = pageable,
                responder = responder
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun findByFirstName_NotAdmin_Failure() {
        // Update the contexts roles to be an empty list
        context.currentRoles = mutableListOf()
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate the wrapper object
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Declare the first name parameter
        val firstName = "Cody"
        // Declare the pageable object
        val pageable = PageRequest.of(0, 5)
        // Declare a concrete responder
        val responder = object : PageResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: Page<PersonInfo>) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        // This should fail on precondition, so set the flag
        shouldFailOnPrecondition = true
        // Call the wrapper factory object and execute the command
        wrapper.factory(failure).findByFirstName(
                firstName = firstName,
                pageable = pageable,
                responder = responder
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun findByLastName_Admin_Success() {
        // Update the current roles to have the role ADMIN
        context.currentRoles = mutableListOf(UserRole.ADMIN)
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate the wrapper object
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Declare the search parameter
        val lastName = "Spath"
        // Instantiate the pageable object
        val pageable = PageRequest.of(0, 5)
        // Instantiate a concrete responder
        val responder = object : PageResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: Page<PersonInfo>) {
                // Mark executed true
                executed = true
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        // Call the wrapper factory method and execute the command
        wrapper.factory(failure).findByLastName(
                lastName = lastName,
                pageable = pageable,
                responder = responder
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

    @Test
    fun findByLastName_NotAdmin_Failure() {
        // Update the context roles to be an empty list
        context.currentRoles = mutableListOf()
        // Instantiate the factory object
        val factory = BasePersonFactory(personRepo, userRepo)
        // Instantiate the wrapper object
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        // Declare the search parameter
        val lastName = "Spath"
        // Declare the pageable object
        val pageable = PageRequest.of(0, 5)
        // Instantiate a concrete responder object
        val responder = object : PageResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: Page<PersonInfo>) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        // This should fail on precondition, so set the flag
        shouldFailOnPrecondition = true
        // Call the wrapper factory method and execute the command
        wrapper.factory(failure).findByLastName(
                lastName = lastName,
                pageable = pageable,
                responder = responder
        ).execute()
        // Make sure the correct scenario occurred
        assertTrue(executed)
    }

}