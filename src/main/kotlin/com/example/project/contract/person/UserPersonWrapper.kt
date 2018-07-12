package com.example.project.contract.person

import com.example.project.contract.Command
import com.example.project.contract.responder.*
import com.example.project.controller.security.UserContext
import com.example.project.controller.security.UserContextWrapper
import com.example.project.controller.security.UserPreconditionFailure
import com.example.project.controller.security.UserRole
import com.example.project.repository.person.IPersonRepository
import com.example.project.repository.user.IUserRepository
import org.springframework.data.domain.Pageable

/**
 * Concrete implementation of the [UserPersonWrapper] that takes a [PersonFactory]
 * and will return an extended factory based off of the [BasePersonFactory]
 * @property context the [UserContext]
 * @property factory the [PersonFactory]
 * @property personRepo the [IPersonRepository]
 * @property userRepo the [IUserRepository]
 */
class UserPersonWrapper(
        private val context: UserContext,
        private val factory: PersonFactory,
        private val personRepo: IPersonRepository,
        private val userRepo: IUserRepository
): UserContextWrapper<PersonFactory> {
    /**
     * Override of the factory method that creates a [PersonFactory] object that will handle
     * roles and permission checking
     */
    override fun factory(userPreconditionFailure: UserPreconditionFailure): PersonFactory {
        return object : PersonFactory {
            /**
             * Returns private retrieve method that handles roles and permissions checking
             */
            override fun retrieve(id: Long, responder: RetrieveResponder<PersonInfo, ErrorTag>): Command {
                return retrieve(
                        id = id,
                        responder = responder,
                        failure = userPreconditionFailure
                )
            }

            /**
             * Returns private create method that handles roles and permissions checking
             */
            override fun create(request: Create.Request, responder: CreateResponder<ErrorTag>): Command {
                return create(
                        request = request,
                        responder = responder,
                        failure = userPreconditionFailure
                )
            }

            /**
             * Returns private update method that handles roles and permissions checking
             */
            override fun update(request: Update.Request, responder: UpdateResponder<ErrorTag>): Command {
                return update(
                        request = request,
                        responder = responder,
                        failure = userPreconditionFailure
                )
            }

            /**
             * Returns private delete method that handles roles and permissions checking
             */
            override fun delete(id: Long, responder: DeleteResponder<ErrorTag>): Command {
                return delete(
                        id = id,
                        responder = responder,
                        failure = userPreconditionFailure
                )
            }

            override fun list(userId: Long, responder: ListResponder<PersonInfo, ErrorTag>): Command {
                return list(
                        userId = userId,
                        responder = responder,
                        failure = userPreconditionFailure
                )
            }

            /**
             * Returns private findByFirstName method that handles roles and permissions checking
             */
            override fun findByFirstName(firstName: String, pageable: Pageable, responder: PageResponder<PersonInfo, ErrorTag>): Command {
                return findByFirstName(
                        firstName = firstName,
                        pageable = pageable,
                        responder = responder,
                        failure = userPreconditionFailure
                )
            }

            /**
             * Returns private findByLastName method that handles roles and permissions checking
             */
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

    /**
     * Private retrieve implementation of the factory retrieve method that handles roles
     * and permissions checking. It makes sure the id of the owner of the person object
     * we wish to retrieve matches the id of the currently logged in user
     */
    private fun retrieve(id: Long, responder: RetrieveResponder<PersonInfo, ErrorTag>, failure: UserPreconditionFailure): Command {
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

    /**
     * Private create implementation of the factory create method that handles roles and
     * permissions checking. It makes sure the id of the currently logged in user matches
     * the id in the request object. It also makes sure the user has a role of "USER"
     */
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

    /**
     * Private update implementation of the factory update method that handles roles
     * and permissions checking. It checks to make sure the id of the owner of the person
     * object we wish to update matches the id of the currently logged in user.
     * It also requires that a user has a role of "USER"
     */
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

    /**
     * Private delete implementation of the factory delete method that handles roles
     * and permissions checking. It checks to make sure the id of the owner of the person
     * we wish to delete matches the id of the currently logged in user. It also requires
     * that a user has a role of "USER"
     */
    private fun delete(id: Long, responder: DeleteResponder<ErrorTag>, failure: UserPreconditionFailure): Command {
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

    /**
     * Private delete implementation of the factory delete method that handles roles
     * and permissions checking. It checks to make sure the id of the owner of the list
     * of people matches the id of the currently logged in user. It also requires
     * that a user has a role of "USER"
     */
    private fun list(userId: Long, responder: ListResponder<PersonInfo, ErrorTag>, failure: UserPreconditionFailure): Command {
        val theUser = userRepo.findById(userId)
        return if (theUser.isPresent && theUser.get().id == context.currentUserId()) {
            context.require(
                    requiredRoles = listOf(UserRole.USER),
                    successCommand = factory.list(userId, responder),
                    failureCommand = failure
            )
        } else {
            return failure
        }
    }

    /**
     * Private findByFirstName implementation of the factory findByFirstName method
     * that handles roles and permissions checking. It requires that the logged in user
     * has a role of "ADMIN"
     */
    private fun findByFirstName(firstName: String, pageable: Pageable, responder: PageResponder<PersonInfo, ErrorTag>, failure: UserPreconditionFailure): Command {
        return context.require(
                requiredRoles = listOf(UserRole.ADMIN),
                successCommand = factory.findByFirstName(firstName, pageable, responder),
                failureCommand = failure
        )
    }

    /**
     * Private findByLastName implementation of the factory findByLastName method
     * that handles roles and permissions checking. It requires that the logged in user
     * has a role of "ADMIN"
     */
    private fun findByLastName(lastName: String, pageable: Pageable, responder: PageResponder<PersonInfo, ErrorTag>, failure: UserPreconditionFailure): Command {
        return context.require(
                requiredRoles = listOf(UserRole.ADMIN),
                successCommand = factory.findByLastName(lastName, pageable, responder),
                failureCommand = failure
        )
    }
}