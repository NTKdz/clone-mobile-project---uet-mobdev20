package com.sprint_sync_server.services

import com.sprint_sync_server.domains.Attachment
import com.sprint_sync_server.domains.Comment
import com.sprint_sync_server.domains.Member
import com.sprint_sync_server.domains.Task
import com.sprint_sync_server.dtos.CommentDto
import com.sprint_sync_server.dtos.response.CommentResDto
import com.sprint_sync_server.repositories.CommentRepository
import org.springframework.stereotype.Service

@Service
class CommentService(commentRepository: CommentRepository) : AbstractService<Comment>(commentRepository) {
	fun getByTaskId(id: String): List<Comment>? {
		val task = getById(id, Task::class.java) ?: return null
		return task.comments?.let { getByIds(it) } ?: emptyList()
	}

	fun getCommentResDto(comment: Comment): CommentResDto {
		val commenter = getById(comment.commenter, Member::class.java)!!
		val attachments = comment.attachments?.let { getByIds(it, Attachment::class.java) }

		return comment.run {
			CommentResDto(
				id = id.toString(),
				content = content,
				commenter = commenter.toDto(),
				createdAt = createdAt,
				attachments = attachments?.map { it.toDto() }
			)
		}
	}

	fun addCommentToTask(uid: String, comment: CommentDto, taskId: String): CommentResDto {
		val commenter = getOne("uid", uid, Member::class.java) ?: throw Exception("Member not found")
		val task = getById(taskId, Task::class.java) ?: throw Exception("Task not found")

		val addedComment = save(comment.also { it.commenter = commenter.id.toString() }.toDomain()).also {
			save(task.apply { comments = comments?.plus(it.id) ?: listOf(it.id) })
		}

		return getCommentResDto(addedComment)
	}
}