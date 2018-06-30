package com.example.project.contract

import com.example.project.contract.security.UserPreconditionFailure
import com.example.project.contract.security.UserPreconditionFailureResponder
import com.example.project.contract.security.UserPreconditionFailureTag
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import org.junit.Assert
import org.junit.Before

abstract class BaseUserWrapperTest : BaseCRUDTest() {
    var executed = false
    var shouldFailOnPrecondition = false

    val failure = object : UserPreconditionFailure {
        override val responder = object  : UserPreconditionFailureResponder {
            override fun onFailure(errors: Multimap<UserPreconditionFailureTag, String>) {
                if (shouldFailOnPrecondition)
                    executed = true
                else
                    Assert.fail("Should not fail on precondition")
            }
        }
        override val errors = HashMultimap.create<UserPreconditionFailureTag, String>()
        override fun addError(error: Pair<UserPreconditionFailureTag, String>) {
            errors.put(error.first, error.second)
        }
        override fun addErrors(errors: Multimap<UserPreconditionFailureTag, String>) {
            this.errors.putAll(errors)
        }
    }

    @Before
    fun initialize() {
        executed = false
        shouldFailOnPrecondition = false
    }
}