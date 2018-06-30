package com.example.project.contract.user

import com.example.project.contract.responder.RetrieveResponder
import com.example.project.repository.user.IUserRepository
import com.example.project.repository.user.User
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
@RunWith(SpringRunner::class)
class FindByUsernameOrEmailTest {

    @Autowired
    private lateinit var userRepo: IUserRepository

    private var username = ""
    private var email = ""
    private var id = -1L

    @Before
    fun init() {
        val user = userRepo.save(User("username", "email@address.com", "password123"))
        username = user.username
        email = user.email
        id = user.id
    }

    @After
    fun tearDown() {
        userRepo.deleteAll()
    }

    @Test
    fun retrieveUserByUsername_Success() {
        var executed = false
        val responder = object : RetrieveResponder<UserInfo> {
            override fun onSuccess(t: UserInfo) {
                executed = true
                assertEquals(t.id, id)
            }

            override fun onFailure(e: String) {
                fail("Should not fail")
            }
        }
        FindByUsernameOrEmail(
                target = username,
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun retrieveUserByEmail_Success() {
        var executed = false
        val responder = object : RetrieveResponder<UserInfo> {
            override fun onSuccess(t: UserInfo) {
                executed = true
                assertEquals(t.id, id)
            }

            override fun onFailure(e: String) {
                fail("Should not fail")
            }
        }
        FindByUsernameOrEmail(
                target = email,
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

    @Test
    fun retrieve_UsernameAndEmailNotFound_Failure() {
        var executed = false
        val responder = object : RetrieveResponder<UserInfo> {
            override fun onSuccess(t: UserInfo) {
                fail("Should not succeed")
            }

            override fun onFailure(e: String) {
                executed = true
            }
        }
        FindByUsernameOrEmail(
                target = "invalidUsername",
                responder = responder,
                userRepo = userRepo
        ).execute()
        assertTrue(executed)
    }

}