package com.sprint_sync_server.dtos

import com.sprint_sync_server.domains.Attachment
import org.bson.types.Binary
import org.bson.types.ObjectId

data class AttachmentDto(
	val id: String? = null,
	val name: String,
	val fileType: String,
	val fileSize: Long,
	val createdAt: String,
	val content: ByteArray,
	val project: String,
) : IDto {
	override fun toDomain() = Attachment(
		id = id?.let { ObjectId(it) } ?: ObjectId(),
		name = name,
		fileType = fileType,
		fileSize = fileSize,
		createdAt = createdAt,
		content = Binary(content),
		project = ObjectId(project),
	)
}