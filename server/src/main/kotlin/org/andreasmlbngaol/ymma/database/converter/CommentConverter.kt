package org.andreasmlbngaol.ymma.database.converter

import org.andreasmlbngaol.ymma.database.tables.Comments
import org.andreasmlbngaol.ymma.domains.comment.Comment
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toComment() = Comment(
    id = this[Comments.id].value,
    postId = this[Comments.post].value,
    authorId = this[Comments.author].value,
    content = this[Comments.content],
    parentId = this[Comments.parent]?.value,
    createdAt = this[Comments.createdAt].toString(),
    updatedAt = this[Comments.updatedAt].toString(),
)