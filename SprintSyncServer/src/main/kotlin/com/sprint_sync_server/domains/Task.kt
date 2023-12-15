package com.sprint_sync_server.domains

import com.sprint_sync_server.dtos.TaskDto
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("tasks")
data class Task(
    @Id
	@Field("_id")
	val id: ObjectId,
    val name: String,
    val description: String? = null,
    val sprint: ObjectId,
    val team: ObjectId,
    var assignor: ObjectId,
    val assignees: List<ObjectId>,
    val parentTask: ObjectId? = null,
    val attachments: List<ObjectId>? = null,
    val statusIndex: Int,
    val deadline: String? = null,
    val point: Int = 0,
    var comments: List<ObjectId>? = null,
    val labels: List<String>? = null,
    val createdAt: String,
    val completedAt: String? = null
) : IDomain {
	override fun toDto() = TaskDto(
		id = id.toString(),
		name = name,
		description = description,
		sprint = sprint.toString(),
		team = team.toString(),
		assignor = assignor.toString(),
		assignees = assignees.map { it.toString() },
		parentTask = parentTask?.toString(),
		attachments = attachments?.map { it.toString() },
		statusIndex = statusIndex,
		deadline = deadline,
		point = point,
		comments = comments?.map { it.toString() },
		labels = labels
	)
}