package com.sprint_sync_server.dtos

import com.sprint_sync_server.domains.Sprint
import org.bson.types.ObjectId

data class SprintDto(
	val id: String? = null,
	val isActive: Boolean,
	val startDate: String,
	val endDate: String,
	val completeDate: String? = null,
	val sprintNumber: Int,
	val project: String
) : IDto {
	override fun toDomain() = Sprint(
		id = id?.let { ObjectId(it) } ?: ObjectId(),
		isActive = isActive,
		startDate = startDate,
		endDate = endDate,
		completeDate = completeDate,
		sprintNumber = sprintNumber,
		project = ObjectId(project)
	)
}