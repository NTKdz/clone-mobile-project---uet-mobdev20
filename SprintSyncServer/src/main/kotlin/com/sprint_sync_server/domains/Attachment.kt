package com.sprint_sync_server.domains

import com.sprint_sync_server.dtos.AttachmentDto
import org.bson.types.Binary
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("attachments")
data class Attachment(
	@Id
	@Field("_id")
	val id: ObjectId,
	val name: String,
	val fileType: String,
	val fileSize: Long,
	val createdAt: String,
	val content: Binary,
	val project: ObjectId,
) : IDomain {
	override fun toDto() = AttachmentDto(
		id = id.toString(),
		name = name,
		fileType = fileType,
		fileSize = fileSize,
		createdAt = createdAt,
		content = content.data,
		project = project.toString(),
	)
}