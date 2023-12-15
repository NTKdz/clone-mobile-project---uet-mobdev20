package com.sprint_sync_server.dtos

import com.sprint_sync_server.domains.Team
import org.bson.types.ObjectId

data class TeamDto(
	val id: String? = null,
	val name: String,
	val leader: String,
	val members: List<String>,
	val project: String
) : IDto {
	override fun toDomain() = Team(
		id = id?.let { ObjectId(it) } ?: ObjectId(),
		name = name,
		leader = ObjectId(leader),
		members = members.map { ObjectId(it) },
		project = ObjectId(project)
	)
}