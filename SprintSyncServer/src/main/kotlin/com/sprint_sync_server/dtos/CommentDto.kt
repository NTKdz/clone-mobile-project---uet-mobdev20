package com.sprint_sync_server.dtos

import com.sprint_sync_server.domains.Comment
import org.bson.types.ObjectId

data class CommentDto(
	val id: String? = null,
	var commenter: String? = null,
	val content: String,
	val createdAt: String,
	val attachments: List<String>? = null,
) : IDto {
	override fun toDomain() = Comment(
		id = id?.let { ObjectId(it) } ?: ObjectId(),
		commenter = if (commenter == null) ObjectId() else ObjectId(commenter),
		content = content,
		createdAt = createdAt,
		attachments = attachments?.map { ObjectId(it) },
	)
}