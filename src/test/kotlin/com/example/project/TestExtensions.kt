package com.example.project

fun String.repeat(count: Int): String {
    var str = this
    (0..count).forEach { str += it }
    return str
}