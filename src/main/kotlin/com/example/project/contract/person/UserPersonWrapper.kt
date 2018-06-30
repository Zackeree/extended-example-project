package com.example.project.contract.person

import com.example.project.contract.Command
import com.example.project.contract.responder.*
import com.example.project.contract.security.UserContext
import com.example.project.contract.security.UserContextWrapper
import com.example.project.contract.security.UserPreconditionFailure
import com.example.project.contract.security.UserRole
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.user.IUserRepository
import org.springframework.data.domain.Pageable

class UserPersonWrapper(
        private val context: UserContext,
        private val factory: PersonFactory,
        private val personRepo: IPersonRepository,
        private val userRepo: IUserRepository
): UserContextWrapper<PersonFactory> {
    override fun factory(userPreconditionFailure: UserPreconditionFailure): PersonFactory {
        return object : PersonFactory {
            override fun retrieve(id: Long, responder: RetrieveResponder<PersonInfo>): Command {
                return retrieve(
                        id = id,
                        responder = responder,
                        failure = userPreconditionFailure
                )
            }

            override fun create(request: Create.Request, responder: CreateResponder<ErrorTag>): Command {
                return create(
                        request = request,
                        responder = responder,
                        failure = userPreconditionFailure
                )
            }

            override fun update(request: Update.Request, responder: UpdateResponder<ErrorTag>): Command {
                return update(
                        request = request,
                        responder = responder,
                        failure = userPreconditionFailure
                )
            }

            override fun delete(id: Long, responder: DeleteResponder): Command {
                return delete(
                        id = id,
                        responder = responder,
                        failure = userPreconditionFailure
                )
            }

            override fun findByFirstName(firstName: String, pageable: Pageable, responder: PageResponder<PersonInfo, ErrorTag>): Command {
                return findByFirstName(
                        firstName = firstName,
                        pageable = pageable,
                        responder = responder,
                        failure = userPreconditionFailure
                )
            }

            override fun findByLastName(lastName: String, pageable: Pageable, responder: PageResponder<PersonInfo, ErrorTag>): Command {
                return findByLastName(
                        lastName = lastName,
                        pageable = pageable,
                        responder = responder,
                        failure = userPreconditionFailure
                )
            }
        }
    }

    private fun retrieve(id: Long, responder: RetrieveResponder<PersonInfo>, failure: UserPreconditionFailure): Command {
        val theUser = personRepo.findById(id).get().user
        return if (theUser?.id == context.currentUserId()) {
            context.require(
                    requiredRoles = listOf(),
                    successCommand = factory.retrieve(id = id, responder = responder),
                    failureCommand = failure
            )
        } else {
            return failure
        }
    }

    private fun create(request: Create.Request, responder: CreateResponder<ErrorTag>, failure: UserPreconditionFailure): Command {
        val theUser = userRepo.findById(request.userId).get()
        return if (theUser.id == context.currentUserId()) {
            context.require(
                    requiredRoles = listOf(UserRole.USER),
                    successCommand = factory.create(request, responder),
                    failureCommand = failure
            )
        } else {
            return failure
        }
    }

    private fun update(request: Update.Request, responder: UpdateResponder<ErrorTag>, failure: UserPreconditionFailure): Command {
        val theUser = personRepo.findById(request.id).get().user
        return if (theUser?.id == context.currentUserId()) {
            context.require(
                    requiredRoles = listOf(UserRole.USER),
                    successCommand = factory.update(request, responder),
                    failureCommand = failure
            )
        } else {
            return failure
        }
    }

    private fun delete(id: Long, responder: DeleteResponder, failure: UserPreconditionFailure): Command {
        val theUser = personRepo.findById(id).get().user
        return if (theUser?.id == context.currentUserId()) {
            context.require(
                    requiredRoles = listOf(UserRole.USER),
                    successCommand = factory.delete(id, responder),
                    failureCommand = failure
            )
        } else {
            return failure
        }
    }

    private fun findByFirstName(firstName: String, pageable: Pageable, responder: PageResponder<PersonInfo, ErrorTag>, failure: UserPreconditionFailure): Command {
        return context.require(
                requiredRoles = listOf(UserRole.ADMIN),
                successCommand = factory.findByFirstName(firstName, pageable, responder),
                failureCommand = failure
        )
    }

    private fun findByLastName(lastName: String, pageable: Pageable, responder: PageResponder<PersonInfo, ErrorTag>, failure: UserPreconditionFailure): Command {
        return context.require(
                requiredRoles = listOf(UserRole.ADMIN),
                successCommand = factory.findByLastName(lastName, pageable, responder),
                failureCommand = failure
        )
    }
}