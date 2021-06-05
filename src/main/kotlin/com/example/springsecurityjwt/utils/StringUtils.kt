package com.example.springsecurityjwt.utils

fun String.camelToSnake(): String = replace(Regex("([a-z])([A-Z]+)"), "$1_$2")
