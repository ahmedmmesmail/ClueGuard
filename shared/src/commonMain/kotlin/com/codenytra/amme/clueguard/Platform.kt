package com.codenytra.amme.clueguard

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform