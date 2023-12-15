package com.sprint_sync_server.dtos.response

import com.sprint_sync_server.dtos.AttachmentDto
import com.sprint_sync_server.dtos.MemberDto

data class CommentResDto(
    val id: String? = null,
    val commenter: MemberDto,
    val content: String,
    val createdAt: String,
    val attachments: List<AttachmentDto>? = null,
)
