package com.example.project

import com.example.project.contract.user.UserInfo
import com.google.common.collect.Multimap

fun <T: Enum<T>>Multimap<T, String>.toStringMap(): Map<String, Collection<String>> {
    return this.asMap().mapKeys { it.key.name }
}

fun String?.isNullOrBlankOrNotEmail(): Boolean {
    return if (this.isNullOrBlank()) true else return !UserInfo.isEmailValid(this!!)
}