package org.andreasmlbngaol.ymma.database.converter

import org.andreasmlbngaol.ymma.database.tables.Posts
import org.andreasmlbngaol.ymma.domains.post.Post
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toPost() = Post(
    id = this[Posts.id].value,
    courseId = this[Posts.course].value,
    authorId = this[Posts.author]?.value,
    content = this[Posts.content],
    type = this[Posts.type],
    createdAt = this[Posts.createdAt].toString()
)