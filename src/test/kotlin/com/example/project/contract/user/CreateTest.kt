package com.example.project.contract.user

import com.example.project.contract.responder.CreateResponder
import com.example.project.repository.user.IUserRepository
import com.google.common.collect.Multimap
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
@RunWith(SpringRunner::class)
class CreateTest {

    @Autowired
    private lateinit var userRepo: IUserRepository

    private val baseRequest = Create.Request(
            username = "username",
            email = "email@address.com",
            password = "password1234",
            passwordConfirm = "password1234"
    )

    @Test
    fun usernameBlank_Failure() {
        var executed = false
        val request = baseRequest.copy(username = "")
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
                assertTrue(e[ErrorTag.USERNAME].isNotEmpty())
            }
        }
        Create(
                request = request,
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun usernameIsTaken_Failure() {
        var executed = false
        userRepo.save(baseRequest.toEntity())
        assertEquals(userRepo.count(), 1)
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
                assertTrue(e[ErrorTag.USERNAME].isNotEmpty())
            }
        }
        Create(
                request = baseRequest.copy(email = "michaelscott@dundermifflin.com"),
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun userNameTooLong_Failure() {
        var executed = false
        val superName = "no no no no no".repeat(15)
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
                assertTrue(e[ErrorTag.USERNAME].isNotEmpty())
            }
        }
        Create(
                request = baseRequest.copy(username = superName),
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun emailAddressIsBlank_Failure() {
        var executed = false
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed.")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
                assertTrue(e[ErrorTag.EMAIL_ADDRESS].isNotEmpty())
            }
        }
        Create(
                request = baseRequest.copy(email = ""),
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun emailAddressIsTaken_Failure() {
        var executed = false
        userRepo.save(baseRequest.toEntity())
        assertEquals(userRepo.count(), 1)
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed.")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
                assertTrue(e[ErrorTag.EMAIL_ADDRESS].isNotEmpty())
            }
        }
        Create(
                request = baseRequest.copy(username = "aDifferentUsername"),
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun emailAddressTooLong_Failure() {
        var executed = false
        val superEmail = "no no no no no".repeat(15)
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
                assertTrue(e[ErrorTag.EMAIL_ADDRESS].isNotEmpty())
            }
        }
        Create(
                request = baseRequest.copy(email = superEmail),
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun passwordsDoNotMatch_Success() {
        var executed = false
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                fail("Should not succeed")
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                executed = true
                assertTrue(e[ErrorTag.PASSWORD_CONFIRM].isNotEmpty())
            }
        }
        Create(
                request = baseRequest.copy(password = "passwrod", passwordConfirm = "password"),
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun create_Success() {
        var executed = false
        val responder = object : CreateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                executed = true
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                fail("Should not fail.")
            }
        }
        Create(
                request = baseRequest,
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
        assertEquals(userRepo.count(), 1)
    }

}