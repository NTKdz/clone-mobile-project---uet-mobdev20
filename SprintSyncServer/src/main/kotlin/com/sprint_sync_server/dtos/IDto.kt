package com.sprint_sync_server.dtos

import com.sprint_sync_server.domains.IDomain

interface IDto {
	fun toDomain(): IDomain
}