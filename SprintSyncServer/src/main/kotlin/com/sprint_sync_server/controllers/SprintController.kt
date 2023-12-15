package com.sprint_sync_server.controllers

import com.sprint_sync_server.dtos.SprintDto
import com.sprint_sync_server.dtos.response.ApiResponse
import com.sprint_sync_server.dtos.response.ReportChartDto
import com.sprint_sync_server.services.SprintService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sprints")
class SprintController(private val sprintService: SprintService) {
	@GetMapping("/{id}")
	fun getSprint(@PathVariable id: String): ApiResponse<SprintDto> {
		return when (val sprint = sprintService.getById(id)) {
			null -> ApiResponse(err = "Sprint not found")
			else -> ApiResponse(sprint.toDto())
		}
	}

	@GetMapping("/project/{id}")
	fun getSprintsOfProject(@PathVariable id: String): ApiResponse<List<SprintDto>> {
		return when (val sprints = sprintService.getByProjectId(id)) {
			null -> ApiResponse(err = "Project not found")
			else -> ApiResponse(sprints.map { it.toDto() })
		}
	}

	@GetMapping("/active/project/{id}")
	fun getActiveSprintOfProject(@PathVariable id: String): ApiResponse<SprintDto> {
		return when (val sprints = sprintService.getByProjectId(id)) {
			null -> ApiResponse(err = "Project not found")
			else -> {
				val activeSprint = sprints.firstOrNull { it.isActive }
				when (activeSprint) {
					null -> ApiResponse(err = "Active sprint not found")
					else -> ApiResponse(activeSprint.toDto())
				}
			}
		}
	}

	@GetMapping("/report/{id}")
	suspend fun getSprintReport(@PathVariable id: String): ApiResponse<ReportChartDto> {
		return try {
			ApiResponse(sprintService.getSprintReport(id))
		}
		catch (e: Exception) {
			ApiResponse(err = e.message)
		}
	}

	@PostMapping
	fun addSprint(@RequestBody dto: SprintDto): ApiResponse<SprintDto> {
		return try {
			sprintService.checkDates(dto.startDate, dto.endDate)
			if (sprintService.existsById(dto.id)) throw Exception("Sprint already exists")
			ApiResponse(sprintService.save(dto.toDomain()).toDto())
		}
		catch (err: Exception) {
			ApiResponse(err = err.message)
		}
	}

	@PatchMapping
	fun updateSprint(@RequestBody dto: SprintDto): ApiResponse<SprintDto> {
		return try {
			sprintService.checkDates(dto.startDate, dto.endDate)
			ApiResponse(sprintService.updateSprint(dto).toDto())
		}
		catch (e: Exception) {
			ApiResponse(err = e.message)
		}
	}

	@DeleteMapping("/{id}")
	fun deleteSprint(@PathVariable id: String): ApiResponse<String> {
		return when (sprintService.existsById(id)) {
			true -> {
				sprintService.deleteById(id)
				ApiResponse("Sprint deleted")
			}

			else -> ApiResponse(err = "Sprint not found")
		}
	}
}