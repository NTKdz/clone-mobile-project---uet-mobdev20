package com.sprint_sync_server.controllers

import com.sprint_sync_server.dtos.MemberDto
import com.sprint_sync_server.dtos.response.ApiResponse
import com.sprint_sync_server.services.MemberService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val memberService: MemberService) {
	@PostMapping("/sign_up")
	fun signUp(@RequestBody dto: MemberDto): ApiResponse<String> {
		return when (val member = memberService.getByUid(dto.uid)) {
			null -> {
				val id = memberService.save(dto.toDomain()).id.toString()
				ApiResponse("Signed up successfully\nId: $id")
			}

			else -> ApiResponse("Authenticated with Google\nId: ${member.id}")
		}
	}
}