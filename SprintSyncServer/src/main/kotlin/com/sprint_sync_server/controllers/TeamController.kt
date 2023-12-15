package com.sprint_sync_server.controllers

import com.sprint_sync_server.dtos.MemberDto
import com.sprint_sync_server.dtos.TeamDto
import com.sprint_sync_server.dtos.response.ApiResponse
import com.sprint_sync_server.dtos.response.TeamResDto
import com.sprint_sync_server.services.TeamService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/teams")
class TeamController(private val teamService: TeamService) {
    @GetMapping("/{id}")
    fun getTeam(@PathVariable id: String): ApiResponse<TeamResDto> {
        return when (val team = teamService.getById(id)) {
            null -> ApiResponse(err = "Team not found")
            else -> ApiResponse(teamService.getTeamResDto(team))
        }
    }

    @GetMapping("/project/{id}")
    fun getTeamsOfProject(@PathVariable id: String): ApiResponse<List<TeamResDto>> {
        return when (val teams = teamService.getByProjectId(id)) {
            null -> ApiResponse(err = "Project not found")
            else -> ApiResponse(teams.map { teamService.getTeamResDto(it) })
        }
    }

    @PostMapping
    fun addTeam(@RequestBody dto: TeamDto): ApiResponse<TeamResDto> {
        return when (teamService.existsById(dto.id)) {
            true -> ApiResponse(err = "Team already exists")
            else -> ApiResponse(teamService.getTeamResDto(teamService.save(dto.toDomain())))
        }
    }

    @PostMapping("/add-member/{email}/team/{teamId}")
    fun addMember(
        @PathVariable email: String,
        @PathVariable teamId: String
    ): ApiResponse<TeamResDto> {
        return try {
            ApiResponse(teamService.addMember(email, teamId))
        } catch (e: Exception) {
            ApiResponse(err = e.message)
        }
    }

    @PatchMapping
    fun updateTeam(@RequestBody dto: TeamDto): ApiResponse<TeamResDto> {
        return when (teamService.existsById(dto.id)) {
            true -> ApiResponse(teamService.getTeamResDto(teamService.save(dto.toDomain())))
            else -> ApiResponse(err = "Team not found")
        }
    }

    @DeleteMapping("/{id}")
    fun deleteTeam(@PathVariable id: String): ApiResponse<String> {
        return when (teamService.existsById(id)) {
            true -> {
                teamService.deleteById(id)
                ApiResponse("Team deleted")
            }

            else -> ApiResponse(err = "Team not found")
        }
    }
}