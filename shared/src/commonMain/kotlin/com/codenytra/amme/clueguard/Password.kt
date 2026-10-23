package com.codenytra.amme.clueguard

data class Password(
    val password: String,
    val hash: String = password.hashCode().toString(),
    val platform: String,
)
