package com.sprint_sync_server.domains

import com.sprint_sync_server.dtos.MemberDto
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("members")
data class Member(
	@Id
	@Field("_id")
	val id: ObjectId,
	val uid: String,
	val name: String,
	val email: String,
	val dob: String?,
	var deviceTokens: List<String>? = null
) : IDomain {
	override fun toDto() = MemberDto(
		id = id.toString(),
		uid = uid,
		name = name,
		email = email,
		dob = dob
	)
}