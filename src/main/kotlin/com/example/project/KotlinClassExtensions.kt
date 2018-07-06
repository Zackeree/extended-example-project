package com.example.project

import com.google.common.collect.Multimap

fun <T: Enum<T>>Multimap<T, String>.toStringMap(): Map<String, Collection<String>> {
    return this.asMap().mapKeys { it.key.name }
}