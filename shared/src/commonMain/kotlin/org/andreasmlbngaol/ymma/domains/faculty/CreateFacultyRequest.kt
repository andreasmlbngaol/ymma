package org.andreasmlbngaol.ymma.domains.faculty

import kotlinx.serialization.Serializable

@Serializable
data class CreateFacultyRequest(
    val name: String,
    val abbreviation: String? = null
)
