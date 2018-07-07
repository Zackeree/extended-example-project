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

@DataJpaTest
@SpringBootTest
@RunWith(SpringRunner::class)
class UserPersonWrapperTest : BaseUserWrapperTest() {
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

    private var existingUserId = -1L
    private var existingPersonId = -1L

    @Before
    fun init() {
        userRepo.deleteAll()
        val user = User("username", "email@address.com", "password123")
        userRepo.save(user)
        existingUserId = user.id
        val person = Person("Thom", "Yorke")
        person.user = user
        personRepo.save(person)
        existingPersonId = person.id
    }

    @Test
    fun create_AllRequirements_Success() {
        val context = FakeUserContext()
        context.login(existingUserId)
        val factory = BasePersonFactory(personRepo, userRepo)
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                executed = true
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        wrapper.factory(failure).create(
                request = baseCreateRequest.copy(userId = existingUserId),
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun create_UserNotOwner_Failure() {
        val context = FakeUserContext()
        context.login(existingUserId + 1L)
        val factory = BasePersonFactory(personRepo, userRepo)
        val wrapper  = UserPersonWrapper(context, factory, personRepo, userRepo)
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        shouldFailOnPrecondition = true
        wrapper.factory(failure).create(
                request = baseCreateRequest.copy(userId = existingUserId),
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun create_NotLoggedIn_Failure() {
        val context = FakeUserContext()
        context.currentRoles = mutableListOf()
        val factory = BasePersonFactory(personRepo, userRepo)
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        val responder = object : CreateResponder<ErrorTag> {
            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }

            override fun onSuccess(t: Long) {
                fail("Should fail on precondition")
            }
        }
        shouldFailOnPrecondition = true
        wrapper.factory(failure).create(
                request = baseCreateRequest.copy(userId = existingUserId),
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun update_AllRequirements_Success() {
        val context = FakeUserContext()
        context.login(existingUserId)
        val factory = BasePersonFactory(personRepo, userRepo)
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        val responder = object : UpdateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                executed = true
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        wrapper.factory(failure).update(
                request = baseUpdateRequest.copy(id = existingUserId),
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun update_UserNotLoggedIn_Failure() {
        val context = FakeUserContext()
        context.currentRoles = mutableListOf()
        val factory = BasePersonFactory(personRepo, userRepo)
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        val responder = object : UpdateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        shouldFailOnPrecondition = true
        wrapper.factory(failure).update(
                request = baseUpdateRequest.copy(id = existingPersonId),
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun update_UserNotOwner_Failure() {
        val context = FakeUserContext()
        context.login(existingUserId + 1L)
        val factory = BasePersonFactory(personRepo, userRepo)
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        val responder = object : UpdateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        shouldFailOnPrecondition = true
        wrapper.factory(failure).update(
                request = baseUpdateRequest.copy(id = existingUserId),
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun retrieve_AllRequirements_Success() {
        val context = FakeUserContext()
        context.login(existingUserId)
        val factory = BasePersonFactory(personRepo, userRepo)
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        val responder = object : RetrieveResponder<PersonInfo> {
            override fun onSuccess(t: PersonInfo) {
                executed = true
                assertEquals(t.id, existingPersonId)
            }

            override fun onFailure(e: Multimap<com.example.project.contract.user.ErrorTag, String>) {
                fail("Should")
            }
        }
        wrapper.factory(failure).retrieve(
                id = existingPersonId,
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun retrieve_NotLoggedIn_Failure() {
        val context = FakeUserContext()
        context.currentRoles = mutableListOf()
        val factory = BasePersonFactory(personRepo, userRepo)
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        val responder = object : RetrieveResponder<PersonInfo> {
            override fun onSuccess(t: PersonInfo) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: Multimap<com.example.project.contract.user.ErrorTag, String>) {
                fail("Should")
            }

        }
        shouldFailOnPrecondition = true
        wrapper.factory(failure).retrieve(
                id = existingPersonId,
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun retrieve_UserNotOwner_Failure() {
        val newUser = userRepo.save(User("newUser", "email@gmail.com", "password"))
        val context = FakeUserContext()
        context.login(newUser.id)
        val factory = BasePersonFactory(personRepo, userRepo)
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        val responder = object : RetrieveResponder<PersonInfo> {
            override fun onSuccess(t: PersonInfo) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: Multimap<com.example.project.contract.user.ErrorTag, String>) {
                fail("Should")
            }
        }
        shouldFailOnPrecondition = true
        wrapper.factory(failure).retrieve(
                id = existingPersonId,
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun delete_AllRequirements_Success() {
        val context = FakeUserContext()
        context.login(existingUserId)
        val factory = BasePersonFactory(personRepo, userRepo)
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        val responder = object : DeleteResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                executed = true
                assertEquals(t, existingPersonId)
            }

            override fun onFailure(e: HashMultimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        wrapper.factory(failure).delete(
                id = existingPersonId,
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun delete_NotLoggedIn_Failure() {
        val context = FakeUserContext()
        context.currentRoles = mutableListOf()
        val factory = BasePersonFactory(personRepo, userRepo)
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        val responder = object : DeleteResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: HashMultimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        shouldFailOnPrecondition = true
        wrapper.factory(failure).delete(
                id = existingPersonId,
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun delete_UserNotOwner_Failure() {
        val context = FakeUserContext()
        context.login(existingUserId + 1)
        val factory = BasePersonFactory(personRepo, userRepo)
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        val responder = object : DeleteResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: HashMultimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        shouldFailOnPrecondition = true
        wrapper.factory(failure).delete(
                id = existingPersonId,
                responder = responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun findByFirstName_Admin_Success() {
        val context = FakeUserContext()
        context.currentRoles = mutableListOf(UserRole.ADMIN)
        val factory = BasePersonFactory(personRepo, userRepo)
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        val firstName = "Cody"
        val pageable = PageRequest.of(0, 5)
        val responder = object : PageResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: Page<PersonInfo>) {
                executed = true
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        wrapper.factory(failure).findByFirstName(
                firstName,
                pageable,
                responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun findByFirstName_NotAdmin_Failure() {
        val context = FakeUserContext()
        context.currentRoles = mutableListOf()
        val factory = BasePersonFactory(personRepo, userRepo)
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        shouldFailOnPrecondition = true
        val firstName = "Cody"
        val pageable = PageRequest.of(0, 5)
        val responder = object : PageResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: Page<PersonInfo>) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        wrapper.factory(failure).findByFirstName(
                firstName,
                pageable,
                responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun findByLastName_Admin_Success() {
        val context = FakeUserContext()
        context.currentRoles = mutableListOf(UserRole.ADMIN)
        val factory = BasePersonFactory(personRepo, userRepo)
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        val lastName = "Spath"
        val pageable = PageRequest.of(0, 5)
        val responder = object : PageResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: Page<PersonInfo>) {
                executed = true
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail")
            }
        }
        wrapper.factory(failure).findByLastName(
                lastName,
                pageable,
                responder
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun findByLastName_NotAdmin_Failure() {
        val context = FakeUserContext()
        context.currentRoles = mutableListOf()
        shouldFailOnPrecondition = true
        val factory = BasePersonFactory(personRepo, userRepo)
        val wrapper = UserPersonWrapper(context, factory, personRepo, userRepo)
        val lastName = "Spath"
        val pageable = PageRequest.of(0, 5)
        val responder = object : PageResponder<PersonInfo, ErrorTag> {
            override fun onSuccess(t: Page<PersonInfo>) {
                fail("Should fail on precondition")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should fail on precondition")
            }
        }
        wrapper.factory(failure).findByLastName(
                lastName,
                pageable,
                responder
        ).execute()
        assertTrue(executed)
    }

}