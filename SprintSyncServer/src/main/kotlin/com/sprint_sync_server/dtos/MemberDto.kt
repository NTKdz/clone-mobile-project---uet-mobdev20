package com.sprint_sync_server.dtos

import com.sprint_sync_server.domains.Member
import org.bson.types.ObjectId

data class MemberDto(
	val id: String? = null,
	val uid: String,
	val name: String,
	val email: String,
	val dob: String? = null
) : IDto {
	override fun toDomain() = Member(
		id = id?.let { ObjectId(it) } ?: ObjectId(),
		uid = uid,
		name = name,
		email = email,
		dob = dob
	)
}