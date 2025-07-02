package org.andreasmlbngaol.ymma.domains.college

import kotlinx.serialization.Serializable

@Serializable
data class CreateCollegeResponse(
    val id: Long,
    val name: String,
    val abbreviation: String? = null
)
