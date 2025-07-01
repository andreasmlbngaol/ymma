package org.andreasmlbngaol.ymma

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform