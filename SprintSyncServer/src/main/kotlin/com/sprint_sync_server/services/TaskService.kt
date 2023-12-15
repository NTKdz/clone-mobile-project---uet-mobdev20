package com.sprint_sync_server.services

import com.sprint_sync_server.domains.Member
import com.sprint_sync_server.domains.Notice
import com.sprint_sync_server.domains.Project
import com.sprint_sync_server.domains.Sprint
import com.sprint_sync_server.domains.Task
import com.sprint_sync_server.domains.Team
import com.sprint_sync_server.dtos.response.TaskResDto
import com.sprint_sync_server.repositories.TaskRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class TaskService(
	taskRepository: TaskRepository,
	private val notificationService: NotificationService
) : AbstractService<Task>(taskRepository) {

	fun getByProjectId(id: String): List<Task>? {
		getById(id, Project::class.java) ?: return null

		val sprints = getList("project", ObjectId(id), Sprint::class.java)
		return getList("sprint", null, sprints.map { it.id }, Task::class.java)
	}

	fun getBySprintId(id: String): List<Task>? {
		getById(id, Sprint::class.java) ?: return null
		return getList("sprint", ObjectId(id), Task::class.java)
	}

	fun getByTeamId(id: String): List<Task>? {
		getById(id, Team::class.java) ?: return null
		return getList("team", ObjectId(id), Task::class.java)
	}

	fun getByUid(uid: String): List<Task>? {
		val me = getOne("uid", uid, Member::class.java) ?: return null
		return getList("assignees", me.id, null, Task::class.java)
	}

	fun getSubTasks(id: String): List<Task>? {
		getById(id) ?: return null
		return getList("parentTask", ObjectId(id), Task::class.java)
	}

	suspend fun getTaskResDto(task: Task): TaskResDto = coroutineScope {
		val assignorAsync = async { getById(task.assignor, Member::class.java)!! }
		val assigneesAsync = async { getByIds(task.assignees, Member::class.java) }

		val assignor = assignorAsync.await()
		val assignees = assigneesAsync.await()

		task.toDto().run {
			TaskResDto(
				id = id,
				name = name,
				description = description,
				sprint = sprint,
				team = team,
				assignor = assignor.toDto(),
				assignees = assignees.map { it.toDto() },
				parentTask = parentTask.toString(),
				statusIndex = statusIndex,
				deadline = deadline,
				comments = comments,
				attachments = attachments,
				point = point,
				labels = labels,
			)
		}
	}

	fun addTask(task: Task, uid: String): Task {
		val assignor = getOne("uid", uid, Member::class.java) ?: throw Exception("Assignor not found")
		val newTask = save(task.also { it.assignor = assignor.id })

		val assignees = getByIds(task.assignees, Member::class.java)
		val tokens = assignees.mapNotNull { it.deviceTokens }.flatten()
		if (tokens.isEmpty()) return newTask

		val notice = Notice(
			body = mapOf(
				"taskName" to task.name,
				"taskDescription" to (task.description ?: ""),
				"taskDeadline" to (task.deadline ?: ""),
			),
			tokens = tokens
		)
		notificationService.sendNotification(notice)

		return newTask
	}
}