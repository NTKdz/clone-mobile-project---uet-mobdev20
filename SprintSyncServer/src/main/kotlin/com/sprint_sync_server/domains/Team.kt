package com.sprint_sync_server.domains

import com.sprint_sync_server.dtos.TeamDto
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("teams")
data class Team(
	@Id
	@Field("_id")
	val id: ObjectId,
	val name: String,
	val leader: ObjectId,
	var members: List<ObjectId>,
	val project: ObjectId
) : IDomain {
	override fun toDto() = TeamDto(
		id = id.toString(),
		name = name,
		leader = leader.toString(),
		members = members.map { it.toString() },
		project = project.toString()
	)
}