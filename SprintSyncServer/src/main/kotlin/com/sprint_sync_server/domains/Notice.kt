package com.sprint_sync_server.domains

/**
 * A data class representing a notice, which includes necessary information to send a notification to a user.
 * */
data class Notice(
	val body: Map<String, String>,
	val tokens: List<String>
)