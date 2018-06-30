package com.example.project.contract.security

import com.example.project.contract.Command

/**
 * Provides method definitions for role and permission checking
 */
interface UserContext {

    fun require(
            requiredRoles: List<UserRole>,
            successCommand: Command,
            failureCommand: UserPreconditionFailure
    ): Command

    fun requireAny(
            requiredRoles: List<UserRole>,
            successCommand: Command,
            failureCommand: UserPreconditionFailure
    ): Command

    fun currentUserId(): Long?
}

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
