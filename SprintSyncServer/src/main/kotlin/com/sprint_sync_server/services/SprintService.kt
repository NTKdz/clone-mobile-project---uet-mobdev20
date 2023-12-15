package com.sprint_sync_server.services

import com.sprint_sync_server.domains.Project
import com.sprint_sync_server.domains.Sprint
import com.sprint_sync_server.domains.Task
import com.sprint_sync_server.dtos.SprintDto
import com.sprint_sync_server.dtos.response.ReportChartDto
import com.sprint_sync_server.dtos.response.TaskResDto
import com.sprint_sync_server.repositories.SprintRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class SprintService(
	sprintRepository: SprintRepository,
	private val taskService: TaskService
) : AbstractService<Sprint>(sprintRepository) {
	fun getByProjectId(id: String): List<Sprint>? {
		getById(id, Project::class.java) ?: return null
		return getList("project", ObjectId(id), Sprint::class.java)
	}

	suspend fun getSprintReport(id: String): ReportChartDto {
		val sprint = getById(id) ?: throw Exception("Sprint not found")

		val tasks = getList("sprint", ObjectId(id), Task::class.java)
		if (tasks.isEmpty()) return ReportChartDto(id, sprint.sprintNumber, emptyList())

		val startDate = LocalDateTime.parse(sprint.startDate).toLocalDate()
		val endDate = minOf(LocalDateTime.now(), LocalDateTime.parse(sprint.endDate)).toLocalDate()

		val tasksCreatedAtDay = tasks.groupBy { LocalDateTime.parse(it.createdAt).toLocalDate() }

		val listOfTasks = mutableListOf<List<TaskResDto>>()

		for (i in 0..ChronoUnit.DAYS.between(startDate, endDate)) {
			val tasksAtI = tasksCreatedAtDay[startDate.plusDays(i)] ?: emptyList()
			val taskAtPrevI = listOfTasks.lastOrNull() ?: emptyList()
			listOfTasks.add(tasksAtI.map { taskService.getTaskResDto(it) } + taskAtPrevI)
		}

		return ReportChartDto(id, sprint.sprintNumber, listOfTasks)
	}

	fun updateSprint(sprint: SprintDto): Sprint {
		val sprints = getList("project", ObjectId(sprint.project), Sprint::class.java)
		if (sprint.isActive && sprints.any { it.id.toString() != sprint.id && it.isActive }) {
			throw Exception("There is already an active sprint")
		}

		return save(sprint.toDomain())
	}

	override fun checkDates(vararg dates: String) {
		val (startDate, endDate) = dates.map(LocalDateTime::parse)
		if (startDate > endDate) throw Exception("Start date must be before end date")
	}
}