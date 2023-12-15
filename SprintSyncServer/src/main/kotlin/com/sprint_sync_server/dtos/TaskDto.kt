package com.sprint_sync_server.dtos

import com.sprint_sync_server.domains.Task
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class TaskDto(
	val id: String? = null,
	val name: String,
	val description: String? = null,
	val sprint: String,
	val team: String,
	val assignor: String? = null,
	val assignees: List<String>,
	val parentTask: String? = null,
	val attachments: List<String>? = null,
	val statusIndex: Int,
	val deadline: String? = null,
	val point: Int = 0,
	val comments: List<String>? = null,
	val labels: List<String>? = null
) : IDto {
	override fun toDomain() = Task(
		id = id?.let { ObjectId(it) } ?: ObjectId(),
		name = name,
		description = description,
		sprint = ObjectId(sprint),
		team = ObjectId(team),
		assignor = if (assignor == null) ObjectId() else ObjectId(assignor),
		assignees = assignees.map { ObjectId(it) },
		parentTask = parentTask?.let { if (it == "null") null else ObjectId(it) },
		attachments = attachments?.map { ObjectId(it) },
		statusIndex = statusIndex,
		deadline = deadline,
		point = point,
		comments = comments?.map { ObjectId(it) },
		labels = labels,
		createdAt = LocalDateTime.now().toString()
	)
}