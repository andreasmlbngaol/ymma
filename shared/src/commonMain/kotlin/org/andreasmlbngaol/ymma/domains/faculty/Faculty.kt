package org.andreasmlbngaol.ymma.domains.faculty

import kotlinx.serialization.Serializable

@Serializable
data class Faculty(
    val id: Long,
    val name: String,
    val abbreviation: String? = null
)

