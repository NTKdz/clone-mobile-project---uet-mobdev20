package com.sprint_sync_server.dtos

import com.sprint_sync_server.domains.Project
import org.bson.types.ObjectId

data class ProjectDto(
	val id: String? = null,
	val name: String,
	val code: String,
	val manager: String,
	val statuses: List<String> = listOf("To Do", "In Progress", "Done"),
	val labels: List<String>? = null
) : IDto {
	override fun toDomain() = Project(
		id = id?.let { ObjectId(it) } ?: ObjectId(),
		name = name,
		code = code,
		manager = ObjectId(manager),
		statuses = statuses,
		labels = labels
	)
}