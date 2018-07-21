package com.example.project

import com.example.project.contract.user.UserInfo
import com.example.project.controller.security.AccessReport
import com.google.common.collect.Multimap

fun <T: Enum<T>>Multimap<T, String>.toStringMap(): Map<String, Collection<String>> {
    return this.asMap().mapKeys { it.key.name }
}

fun String?.isNullOrBlankOrEmail(): Boolean {
    return if (this.isNullOrBlank()) true else return !UserInfo.isEmailValid(this!!)
}

fun AccessReport.toStringMap(): Map<String, Collection<String>> {
    val map = mutableMapOf<String, Collection<String>>()
    map["MISSING_ROLES"] = this.missingRoles.map { it.name }
    return map
}