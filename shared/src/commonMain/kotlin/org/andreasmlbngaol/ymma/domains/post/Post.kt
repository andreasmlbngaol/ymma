package org.andreasmlbngaol.ymma.domains.post

import org.andreasmlbngaol.ymma.type.PostType

data class Post(
    val id: Long,
    val courseId: Long,
    val authorId: Long? = null,
    val content: String,
    val type: PostType,
    val createdAt: String
)
