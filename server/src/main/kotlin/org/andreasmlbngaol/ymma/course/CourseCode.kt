package org.andreasmlbngaol.ymma.course

object CourseCode {
    fun generateCode(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..6)
            .map { allowedChars.random() }
            .joinToString("")
    }
}