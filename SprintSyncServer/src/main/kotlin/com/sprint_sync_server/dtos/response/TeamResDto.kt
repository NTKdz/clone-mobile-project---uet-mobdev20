package com.sprint_sync_server.dtos.response

import com.sprint_sync_server.dtos.MemberDto

data class TeamResDto(
	val id: String? = null,
	val name: String,
	val leader: MemberDto,
	val members: List<MemberDto>,
	val project: String
)