package com.sprint_sync_server.controllers

import com.sprint_sync_server.dtos.ProjectDto
import com.sprint_sync_server.dtos.response.ApiResponse
import com.sprint_sync_server.services.ProjectService
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
@RequestMapping("/projects")
class ProjectController(private val projectService: ProjectService) {
	@GetMapping("/{id}")
	fun getProject(@PathVariable id: String): ApiResponse<ProjectDto> {
		return when (val project = projectService.getById(id)) {
			null -> ApiResponse(err = "Project not found")
			else -> ApiResponse(project.toDto())
		}
	}

	@GetMapping("/me")
	fun getMyProjects(@RequestHeader("Fire-id") uid: String): ApiResponse<List<ProjectDto>> {
		return when (val projects = projectService.getByUid(uid)) {
			null -> ApiResponse(err = "Your data not found")
			else -> ApiResponse(projects.map { it.toDto() })
		}
	}

	@PostMapping
	fun addProject(@RequestBody dto: ProjectDto): ApiResponse<ProjectDto> {
		return when (projectService.existsById(dto.id)) {
			true -> ApiResponse(err = "Project already exists")
			else -> ApiResponse(projectService.save(dto.toDomain()).toDto())
		}
	}

	@PatchMapping
	fun updateProject(@RequestBody dto: ProjectDto): ApiResponse<ProjectDto> {
		return when (projectService.existsById(dto.id)) {
			true -> ApiResponse(projectService.save(dto.toDomain()).toDto())
			else -> ApiResponse(err = "Project not found")
		}
	}

	@DeleteMapping("/{id}")
	fun deleteProject(@PathVariable id: String): ApiResponse<String> {
		return when (projectService.existsById(id)) {
			true -> {
				projectService.deleteById(id)
				ApiResponse("Project deleted")
			}

			else -> ApiResponse(err = "Project not found")
		}
	}
}