package com.example.project.contract.user

import com.example.project.contract.responder.UpdateResponder
import com.example.project.repository.user.IUserRepository
import com.example.project.repository.user.User
import com.google.common.collect.Multimap
import org.junit.After
import org.junit.Assert
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
class UpdateTest {
    private var baseRequest = Update.Request(
            id = 1L,
            username = "thom_yorke",
            email = "thomyorke@radiohead.com"
    )

    private var user = User("jonny_greenwood", "jonnygreenwood@gmail.com", "password123")

    @Autowired
    private lateinit var userRepo: IUserRepository

    @Before
    fun init() {
        user = userRepo.save(user)
    }

    @After
    fun tearDown() {
        userRepo.deleteAll()
    }

    @Test
    fun idNotFound_Failure() {
        var executed = false
        buildUpdateCommand(
                request = baseRequest.copy(id = 420L),
                onSuccess = {
                    Assert.fail("Should not happen")
                },
                onFailure = {
                    executed = true
                    Assert.assertTrue((it[ErrorTag.ID].isNotEmpty()))
                }
        ).execute()
        Assert.assertTrue(executed)
    }

    @Test
    fun blankUsername_Failure() {
        var executed = false
        buildUpdateCommand(
                request = baseRequest.copy(
                        id = user.id,
                        username = ""
                ),
                onSuccess = {
                    Assert.fail("Should not succeed.")
                },
                onFailure = {
                    executed = true
                    Assert.assertTrue(it[ErrorTag.USERNAME].isNotEmpty())
                }
        ).execute()
        Assert.assertTrue(executed)
    }

    @Test
    fun usernameTaken_Failure() {
        var executed = false
        userRepo.save(User("thom_yorke", "email@address.com", "password123"))
        val updatedRequest = baseRequest.copy(id = user.id)
        buildUpdateCommand(
                request = updatedRequest,
                onSuccess = {
                    Assert.fail("Should not succeed")
                },
                onFailure = {
                    executed = true
                    Assert.assertTrue(it[ErrorTag.USERNAME].isNotEmpty())
                }
        ).execute()
        Assert.assertTrue(executed)
    }

    @Test
    fun newUsernameTooLong_Failure() {
        var executed = false
        val newUsername = "no no no no no".repeat(10)
        buildUpdateCommand(
                request = baseRequest.copy(
                        id = user.id,
                        username = newUsername
                ),
                onSuccess = {
                    Assert.fail("Should not succeed.")
                },
                onFailure = {
                    executed = true
                    Assert.assertTrue(it[ErrorTag.USERNAME].isNotEmpty())
                }
        ).execute()
        Assert.assertTrue(executed)
    }

    @Test
    fun blankEmail_Failure() {
        var executed = false
        buildUpdateCommand(
                request = baseRequest.copy(
                        id = user.id,
                        email = ""
                ),
                onSuccess = {
                    Assert.fail("Should not succeed.")
                },
                onFailure = {
                    executed = true
                    Assert.assertTrue(it[ErrorTag.EMAIL_ADDRESS].isNotEmpty())
                }
        ).execute()
        Assert.assertTrue(executed)
    }

    @Test
    fun emailTaken_Failure() {
        var executed = false
        userRepo.save(User("someUsername", "thomyorke@radiohead.com", "password123"))
        val updatedRequest = baseRequest.copy(id = user.id)
        buildUpdateCommand(
                request = updatedRequest,
                onSuccess = {
                    Assert.fail("Should not succeed")
                },
                onFailure = {
                    executed = true
                    Assert.assertTrue(it[ErrorTag.EMAIL_ADDRESS].isNotEmpty())
                }
        ).execute()
        Assert.assertTrue(executed)

    }

    @Test
    fun newEmailTooLong_Failure() {
        var executed = false
        val newEmail = "no no no no no".repeat(10)
        buildUpdateCommand(
                request = baseRequest.copy(
                        id = user.id,
                        email = newEmail
                ),
                onSuccess = {
                    Assert.fail("Should not succeed.")
                },
                onFailure = {
                    executed = true
                    Assert.assertTrue(it[ErrorTag.EMAIL_ADDRESS].isNotEmpty())
                }
        ).execute()
        Assert.assertTrue(executed)
    }

    private fun buildUpdateCommand(request: Update.Request, onSuccess: (id: Long)->Unit, onFailure: (Multimap<ErrorTag, String>)->Unit): Update {
        val responder = object : UpdateResponder<ErrorTag> {
            override fun onSuccess(t: Long) {
                onSuccess(t)
            }

            override fun onFailure(e: Multimap<ErrorTag, String>) {
                onFailure(e)
            }
        }

        return Update(request, responder, userRepo)
    }
}

