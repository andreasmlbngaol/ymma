package org.andreasmlbngaol.ymma.domains.college

import kotlinx.serialization.Serializable

@Serializable
data class CreateCollegeRequest(
    val name: String,
    val abbreviation: String? = null
)