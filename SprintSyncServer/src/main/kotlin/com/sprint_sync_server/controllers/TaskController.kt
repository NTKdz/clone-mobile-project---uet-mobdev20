package com.sprint_sync_server.controllers

import com.google.firebase.messaging.FirebaseMessaging
import com.sprint_sync_server.dtos.TaskDto
import com.sprint_sync_server.dtos.response.ApiResponse
import com.sprint_sync_server.dtos.response.TaskResDto
import com.sprint_sync_server.services.TaskService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tasks")
class TaskController(private val taskService: TaskService) {
    @GetMapping("/{id}")
    suspend fun getTask(@PathVariable id: String): ApiResponse<TaskResDto> {
        return when (val task = taskService.getById(id)) {
            null -> ApiResponse(err = "Task not found")
            else -> ApiResponse(taskService.getTaskResDto(task))
        }
    }

    @GetMapping("/{id}/subtasks")
    suspend fun getSubTasks(@PathVariable id: String): ApiResponse<List<TaskResDto>> {
        return when (val tasks = taskService.getSubTasks(id)) {
            null -> ApiResponse(err = "Task not found")
            else -> ApiResponse(tasks.map { taskService.getTaskResDto(it) })
        }
    }

    @GetMapping("/project/{id}")
    suspend fun getTasksOfProject(@PathVariable id: String): ApiResponse<List<TaskResDto>> {
        return when (val tasks = taskService.getByProjectId(id)) {
            null -> ApiResponse(err = "Project not found")
            else -> {
                coroutineScope {
                    val deferredResults = tasks.map { async { taskService.getTaskResDto(it) } }
                    ApiResponse(deferredResults.awaitAll())
                }
            }
        }
    }

    @GetMapping("/sprint/{id}")
    suspend fun getTasksOfSprint(@PathVariable id: String): ApiResponse<List<TaskResDto>> {
        return when (val tasks = taskService.getBySprintId(id)) {
            null -> ApiResponse(err = "Sprint not found")
            else -> {
                coroutineScope {
                    val deferredResults = tasks.map { async { taskService.getTaskResDto(it) } }
                    ApiResponse(deferredResults.awaitAll())
                }
            }
        }
    }

    @GetMapping("/team/{id}")
    suspend fun getTasksOfTeam(@PathVariable id: String): ApiResponse<List<TaskResDto>> {
        return when (val tasks = taskService.getByTeamId(id)) {
            null -> ApiResponse(err = "Team not found")
            else -> {
                coroutineScope {
                    val deferredResults = tasks.map { async { taskService.getTaskResDto(it) } }
                    ApiResponse(deferredResults.awaitAll())
                }
            }
        }
    }

    @GetMapping("/me")
    suspend fun getMyTasks(@RequestHeader("Fire-id") uid: String): ApiResponse<List<TaskResDto>> {
        return when (val tasks = taskService.getByUid(uid)) {
            null -> ApiResponse(err = "Your data not found")
            else -> {
                coroutineScope {
                    val deferredResults = tasks.map { async { taskService.getTaskResDto(it) } }
                    ApiResponse(deferredResults.awaitAll())
                }
            }
        }
    }

    @PostMapping
    suspend fun addTask(@RequestHeader("Fire-id") uid: String, @RequestBody dto: TaskDto): ApiResponse<TaskResDto> {
        return when (taskService.existsById(dto.id)) {
            true -> ApiResponse(err = "Task already exists")
            else -> ApiResponse(taskService.getTaskResDto(taskService.addTask(dto.toDomain(), uid)))
        }
    }

    @PatchMapping
    suspend fun updateTask(@RequestBody dto: TaskDto): ApiResponse<TaskResDto> {
        return when (taskService.existsById(dto.id)) {
            true -> ApiResponse(taskService.getTaskResDto(taskService.save(dto.toDomain())))
            else -> ApiResponse(err = "Task not found")
        }
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: String): ApiResponse<String> {
        return when (taskService.existsById(id)) {
            true -> {
                taskService.deleteById(id)
                ApiResponse("Task deleted")
            }

            else -> ApiResponse(err = "Task not found")
        }
    }
}