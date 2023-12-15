package com.sprint_sync_server.domains

import com.sprint_sync_server.dtos.CommentDto
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("comments")
data class Comment(
	@Id
	@Field("_id")
	val id: ObjectId,
	val commenter: ObjectId,
	val content: String,
	val createdAt: String,
	val attachments: List<ObjectId>? = null,
) : IDomain {
	override fun toDto() = CommentDto(
		id = id.toString(),
		commenter = commenter.toString(),
		content = content,
		createdAt = createdAt,
		attachments = attachments?.map { it.toString() },
	)
}