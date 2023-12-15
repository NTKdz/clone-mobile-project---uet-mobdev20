package com.sprint_sync_server.services

import com.sprint_sync_server.domains.Attachment
import com.sprint_sync_server.domains.Comment
import com.sprint_sync_server.domains.Project
import com.sprint_sync_server.domains.Task
import com.sprint_sync_server.repositories.AttachmentRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class AttachmentService(attachmentRepository: AttachmentRepository) :
	AbstractService<Attachment>(attachmentRepository) {
	fun getByProjectId(id: String): List<Attachment>? {
		getById(id, Project::class.java) ?: return null
		return getList("project", ObjectId(id), Attachment::class.java)
	}

	fun getByTaskId(id: String): List<Attachment>? {
		val task = getById(id, Task::class.java) ?: return null
		return task.attachments?.let { getByIds(it) } ?: emptyList()
	}

	fun getByCommentId(id: String): List<Attachment>? {
		val comment = getById(id, Comment::class.java) ?: return null
		return comment.attachments?.let { getByIds(it) } ?: emptyList()
	}
}