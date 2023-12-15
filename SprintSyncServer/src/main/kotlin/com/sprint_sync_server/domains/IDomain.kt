package com.sprint_sync_server.domains

import com.sprint_sync_server.dtos.IDto

interface IDomain {
	fun toDto(): IDto
}