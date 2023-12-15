package com.sprint_sync_server.controllers

import com.sprint_sync_server.dtos.AttachmentDto
import com.sprint_sync_server.dtos.response.ApiResponse
import com.sprint_sync_server.services.AttachmentService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/attachments")
class AttachmentController(private val attachmentService: AttachmentService) {
	@GetMapping("/{id}")
	fun getAttachment(@PathVariable id: String): ApiResponse<AttachmentDto> {
		return when (val attachment = attachmentService.getById(id)) {
			null -> ApiResponse(err = "Attachment not found")
			else -> ApiResponse(attachment.toDto())
		}
	}

	@GetMapping("/project/{id}")
	fun getAttachmentsOfProject(@PathVariable id: String): ApiResponse<List<AttachmentDto>> {
		return when (val attachments = attachmentService.getByProjectId(id)) {
			null -> ApiResponse(err = "Project not found")
			else -> ApiResponse(attachments.map { it.toDto() })
		}
	}

	@GetMapping("/task/{id}")
	fun getAttachmentsOfTask(@PathVariable id: String): ApiResponse<List<AttachmentDto>> {
		return when (val attachments = attachmentService.getByTaskId(id)) {
			null -> ApiResponse(err = "Task not found")
			else -> ApiResponse(attachments.map { it.toDto() })
		}
	}

	@GetMapping("/comment/{id}")
	fun getAttachmentsOfComment(@PathVariable id: String): ApiResponse<List<AttachmentDto>> {
		return when (val attachments = attachmentService.getByCommentId(id)) {
			null -> ApiResponse(err = "Comment not found")
			else -> ApiResponse(attachments.map { it.toDto() })
		}
	}

	@PostMapping
	fun addAttachment(@RequestBody dto: AttachmentDto): ApiResponse<AttachmentDto> {
		return try {
			attachmentService.checkDates(dto.createdAt)
			if (attachmentService.existsById(dto.id)) throw Exception("Attachment already exists")
			ApiResponse(attachmentService.save(dto.toDomain()).toDto())
		}
		catch (e: Exception) {
			ApiResponse(err = e.message)
		}
	}

	@PatchMapping
	fun updateAttachment(@RequestBody dto: AttachmentDto): ApiResponse<AttachmentDto> {
		return try {
			attachmentService.checkDates(dto.createdAt)
			if (!attachmentService.existsById(dto.id)) throw Exception("Attachment not found")
			ApiResponse(attachmentService.save(dto.toDomain()).toDto())
		}
		catch (e: Exception) {
			ApiResponse(err = e.message)
		}
	}

	@DeleteMapping("/{id}")
	fun deleteAttachment(@PathVariable id: String): ApiResponse<String> {
		return when (attachmentService.existsById(id)) {
			true -> {
				attachmentService.deleteById(id)
				ApiResponse("Attachment deleted")
			}

			else -> ApiResponse(err = "Attachment not found")
		}
	}
}