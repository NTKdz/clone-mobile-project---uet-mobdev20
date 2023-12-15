package com.sprint_sync_server.dtos.response

data class ApiResponse<T>(
	val data: T? = null,
	val err: String? = null
)