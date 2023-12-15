package com.sprint_sync_server.services

import com.sprint_sync_server.domains.Member
import com.sprint_sync_server.domains.Project
import com.sprint_sync_server.domains.Team
import com.sprint_sync_server.repositories.MemberRepository
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class MemberService(memberRepository: MemberRepository) : AbstractService<Member>(memberRepository) {
	fun getByUid(uid: String) = getOne("uid", uid, Member::class.java)

	fun getByProjectId(id: String): List<Member>? {
		val project = getById(id, Project::class.java) ?: return null
		val manager = getById(project.manager)!!

		val teams = getList("project", ObjectId(id), Team::class.java)
		val members = teams.flatMap { getByIds(it.members + it.leader) }

		return (members + manager).distinctBy { it.id }
	}

	fun getByTeamId(id: String): List<Member>? {
		val team = getById(id, Team::class.java) ?: return null
		return getByIds(team.members + team.leader)
	}

	fun getMemberRole(id: String, projectId: String): String {
		val member = getById(id) ?: throw Exception("Member not found")

		val project = getById(projectId, Project::class.java) ?: throw Exception("Project not found")
		if (member.id == project.manager) return "manager"

		val teams = getList("project", ObjectId(projectId), Team::class.java)
		teams.forEach {
			when {
				it.leader == member.id         -> return "leader"
				it.members.contains(member.id) -> return "member"
			}
		}

		return "none"
	}

	fun deleteByUid(uid: String) {
		val query = Query(where("uid").`is`(uid))
		mongoTemplate.remove(query, Member::class.java)
	}
}