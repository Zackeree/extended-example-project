package com.example.project.contract.user

import com.example.project.contract.Command
import com.example.project.contract.responder.CreateResponder
import com.example.project.contract.responder.DeleteResponder
import com.example.project.contract.responder.RetrieveResponder
import com.example.project.contract.responder.UpdateResponder
import com.example.project.controller.security.UserContext
import com.example.project.controller.security.UserContextWrapper
import com.example.project.controller.security.UserPreconditionFailure
import com.example.project.controller.security.UserRole
import com.example.project.repository.role.IUserRoleRepository
import com.example.project.repository.user.IUserRepository

/**
 * Concrete implementation of the [UserContextWrapper] that takes a [UserFactory]
 * and will return an extended factory based off of the [BaseUserFactory]
 * @property context the [UserContext]
 * @property factory the [UserFactory]
 * @property userRepo the [IUserRepository]
 */
class UserUserWrapper(
        private val context: UserContext,
        private val factory: UserFactory,
        private val userRepo: IUserRepository,
        private val userRoleRepo: IUserRoleRepository
): UserContextWrapper<UserFactory> {
    /**
     * Override of factory method the creates a [UserFactory] object that will handle
     * roles and permission checking
     */
    override fun factory(userPreconditionFailure: UserPreconditionFailure): UserFactory {
        return object : UserFactory {
            /**
             * Returns private create method that actually handles the roles and permissions checking
             */
            override fun create(request: Create.Request, responder: CreateResponder<ErrorTag>): Command {
                return Create(
                        request = request,
                        responder = responder,
                        userRepo = userRepo,
                        userRoleRepo = userRoleRepo
                )
            }

            /**
             * Returns private retrieve method that actuall handles the roles and permissions checking
             */
            override fun retrieve(id: Long, responder: RetrieveResponder<UserInfo, ErrorTag>): Command {
                return retrieve(
                        id = id,
                        responder = responder,
                        failure = userPreconditionFailure
                )
            }

            override fun retrieve(request: FindByUsernameOrEmailAndPassword.Request, responder: RetrieveResponder<UserInfo, ErrorTag>): Command {
                return FindByUsernameOrEmailAndPassword(
                        request = request,
                        responder = responder,
                        userRepo = userRepo
                )
            }

            /**
             * Return private update method that actually handles the roles and permissions checking
             */
            override fun update(request: Update.Request, responder: UpdateResponder<ErrorTag>): Command {
                return update(
                        request = request,
                        responder = responder,
                        failure = userPreconditionFailure
                )
            }

            /**
             * Return private delete method that actually handles the roles and permissions checking
             */
            override fun delete(id: Long, responder: DeleteResponder<ErrorTag>): Command {
                return delete(
                        id = id,
                        responder = responder,
                        failure = userPreconditionFailure
                )
            }
        }

    }

    //PRIVATE IMPLEMENTATIONS ONLY NEEDED FOR COMMANDS THAT REQUIRE SPRING SECURITY ROLES/PERMISSIONS CHECKING.
    //ANY COMMANDS THAT DO NOT NEED A USER LOGGED IN IN ORDER TO EXECUTE SHOULD RETURN THE BASE COMMAND OBJECT

    /**
     * Private retrieve implementation of the factory retrieve method that handles role and permission checking
     */
    private fun retrieve(id: Long, responder: RetrieveResponder<UserInfo, ErrorTag>, failure: UserPreconditionFailure): Command {
        return context.require(
                requiredRoles = listOf(),
                successCommand = factory.retrieve(id, responder),
                failureCommand = failure
        )
    }

    /**
     * Private update implementation of the factory update method that handles role and permission checking.
     * It checks to make sure the id of the user that is going to be updated matches the id of the currently
     * logged in user. It also requires that a user has a role of "USER"
     */
    private fun update(request: Update.Request, responder: UpdateResponder<ErrorTag>, failure: UserPreconditionFailure): Command {
        val theUser = userRepo.findById(request.id).get()
        return if (theUser.id == context.currentUserId()) {
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
     * Private delete implementation of the factory delete method that handles role and permission checking.
     * It checks to make sure the id of the user that is going to be deleted matches the id of the currently
     * logged in user. It also requires that a user has a role of "USER"
     */
    private fun delete(id: Long, responder: DeleteResponder<ErrorTag>, failure: UserPreconditionFailure): Command {
        val theUser = userRepo.findById(id).get()
        return if (theUser.id == context.currentUserId()) {
            context.require(
                    requiredRoles = listOf(UserRole.USER),
                    successCommand = factory.delete(id, responder),
                    failureCommand = failure
            )
        } else {
            return failure
        }
    }
}