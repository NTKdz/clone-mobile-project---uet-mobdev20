package com.sprint_sync_server.domains

import com.sprint_sync_server.dtos.SprintDto
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("sprints")
data class Sprint(
	@Id
	@Field("_id")
	val id: ObjectId,
	val isActive: Boolean,
	val startDate: String,
	val endDate: String,
	val completeDate: String? = null,
	val sprintNumber: Int,
	val project: ObjectId
) : IDomain {
	override fun toDto() = SprintDto(
		id = id.toString(),
		isActive = isActive,
		startDate = startDate,
		endDate = endDate,
		completeDate = completeDate,
		sprintNumber = sprintNumber,
		project = project.toString()
	)
}