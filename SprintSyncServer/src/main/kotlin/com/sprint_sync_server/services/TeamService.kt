package com.sprint_sync_server.services

import com.sprint_sync_server.domains.Member
import com.sprint_sync_server.domains.Project
import com.sprint_sync_server.domains.Team
import com.sprint_sync_server.dtos.response.TeamResDto
import com.sprint_sync_server.repositories.TeamRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class TeamService(teamRepository: TeamRepository) : AbstractService<Team>(teamRepository) {
	fun getByProjectId(id: String): List<Team>? {
		getById(id, Project::class.java) ?: return null
		return getList("project", ObjectId(id), Team::class.java)
	}

	fun getTeamResDto(team: Team): TeamResDto {
		val leader = getById(team.leader, Member::class.java)!!
		val members = getByIds(team.members, Member::class.java)

		return team.toDto().run {
			TeamResDto(
				id = id,
				name = name,
				leader = leader.toDto(),
				members = members.map { it.toDto() },
				project = project
			)
		}
	}

	fun addMember(email: String, teamId: String): TeamResDto {
		val team = getById(teamId) ?: throw Exception("Team not found")
		val member = getOne("email", email, Member::class.java) ?: throw Exception("Member not found")

		if (member.id in team.members) throw Exception("Member already in team")
		if (member.id == team.leader) throw Exception("Member is already the leader of this team")

		save(team.also { it.members += member.id })
		return getTeamResDto(team)
	}
}