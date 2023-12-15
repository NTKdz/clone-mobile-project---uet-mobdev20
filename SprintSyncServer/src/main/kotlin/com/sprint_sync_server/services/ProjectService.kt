package com.sprint_sync_server.services

import com.sprint_sync_server.domains.Member
import com.sprint_sync_server.domains.Project
import com.sprint_sync_server.domains.Team
import com.sprint_sync_server.repositories.ProjectRepository
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class ProjectService(projectRepository: ProjectRepository) : AbstractService<Project>(projectRepository) {
	fun getByUid(uid: String): List<Project>? {
		val me = getOne("uid", uid, Member::class.java) ?: return null

		val projects = getList("manager", me.id, Project::class.java)

		val teamQuery = Query(
			Criteria().orOperator(
				where("leader").`is`(me.id),
				where("members").`in`(me.id)
			)
		)
		val teams = mongoTemplate.find(teamQuery, Team::class.java)

		return getByIds(projects.map { it.id } + teams.map { it.project })
	}
}