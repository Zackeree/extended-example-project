package com.example.project.controller.security

import com.example.project.contract.Command
import com.example.project.contract.Executable
import com.example.project.contract.SecuredAction

/**
 * Provides method definitions for role and permission checking
 */
interface UserContext {

    fun require(
            requiredRoles: List<UserRole>,
            successCommand: Command,
            failureCommand: UserPreconditionFailure
    ): Command

    fun <S, E>require(
        requiredRoles: List<UserRole>,
        successCommand: Executable<S, E>
    ): SecuredAction<S, E>

    fun requireAny(
            requiredRoles: List<UserRole>,
            successCommand: Command,
            failureCommand: UserPreconditionFailure
    ): Command

    fun currentUserId(): Long?
}

data class AccessReport(val missingRoles: List<UserRole>)

/**
 * Enum of User Roles
 */
enum class UserRole {
    USER,
    ADMIN
}

/**
 * Enum of User Permissions
 */
enum class PermissionType {
    PHONE_NUMBER_UPDATE
}
