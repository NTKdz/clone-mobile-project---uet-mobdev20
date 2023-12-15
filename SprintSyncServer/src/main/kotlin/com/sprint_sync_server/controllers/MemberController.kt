package com.sprint_sync_server.controllers

import com.sprint_sync_server.dtos.MemberDto
import com.sprint_sync_server.dtos.response.ApiResponse
import com.sprint_sync_server.services.MemberService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/members")
class MemberController(private val memberService: MemberService) {
	@GetMapping("/{id}")
	fun getMember(@PathVariable id: String): ApiResponse<MemberDto> {
		return when (val member = memberService.getById(id)) {
			null -> ApiResponse(err = "Member not found")
			else -> ApiResponse(member.toDto())
		}
	}

	@GetMapping("/project/{id}")
	fun getMembersOfProject(@PathVariable id: String): ApiResponse<List<MemberDto>> {
		return when (val members = memberService.getByProjectId(id)) {
			null -> ApiResponse(err = "Project not found")
			else -> ApiResponse(members.map { it.toDto() })
		}
	}

	@GetMapping("/team/{id}")
	fun getMembersOfTeam(@PathVariable id: String): ApiResponse<List<MemberDto>> {
		return when (val members = memberService.getByTeamId(id)) {
			null -> ApiResponse(err = "Team not found")
			else -> ApiResponse(members.map { it.toDto() })
		}
	}

	@GetMapping("/me")
	fun getMe(@RequestHeader("Fire-id") uid: String): ApiResponse<MemberDto> {
		return when (val me = memberService.getByUid(uid)) {
			null -> ApiResponse(err = "Your data not found")
			else -> ApiResponse(me.toDto())
		}
	}

	@GetMapping("/role/{id}/project/{projectId}")
	fun getMemberRole(@PathVariable id: String, @PathVariable projectId: String): ApiResponse<String> {
		return try {
			ApiResponse(memberService.getMemberRole(id, projectId))
		}
		catch (e: Exception) {
			ApiResponse(err = e.message)
		}
	}

	@PatchMapping("/device/{token}")
	fun addDevice(
		@RequestHeader("Fire-id") uid: String,
		@PathVariable token: String
	): ApiResponse<String> {
		val member = memberService.getByUid(uid) ?: return ApiResponse(err = "Your data not found")
		if (member.deviceTokens?.contains(token) == true) return ApiResponse("Device already added")
		memberService.save(member.apply { deviceTokens = deviceTokens?.plus(token) ?: listOf(token) })
		return ApiResponse("Device added")
	}

	@PatchMapping
	fun updateMember(@RequestBody dto: MemberDto): ApiResponse<MemberDto> {
		return try {
			dto.dob?.let { memberService.checkDates(it) }
			val member = memberService.getById(dto.id) ?: throw Exception("Member not found")
			ApiResponse(memberService.save(dto.toDomain().apply { deviceTokens = member.deviceTokens }).toDto())
		}
		catch (err: Exception) {
			ApiResponse(err = err.message)
		}
	}

	@DeleteMapping("/me")
	fun deleteMe(@RequestHeader("Fire-id") uid: String): ApiResponse<String> {
		return when (memberService.getByUid(uid)) {
			null -> ApiResponse(err = "Your data not found")
			else -> {
				memberService.deleteByUid(uid)
				ApiResponse("Your data deleted")
			}
		}
	}
}