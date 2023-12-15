package com.sprint_sync_server.domains

import com.sprint_sync_server.dtos.ProjectDto
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("projects")
data class Project(
	@Id
	@Field("_id")
	val id: ObjectId,
	val name: String,
	val code: String,
	val manager: ObjectId,
	val statuses: List<String>,
	val labels: List<String>?
) : IDomain {
	override fun toDto() = ProjectDto(
		id = id.toString(),
		name = name,
		code = code,
		manager = manager.toString(),
		statuses = statuses,
		labels = labels
	)
}