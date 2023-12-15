package com.sprint_sync_server.controllers

import com.sprint_sync_server.dtos.CommentDto
import com.sprint_sync_server.dtos.response.ApiResponse
import com.sprint_sync_server.dtos.response.CommentResDto
import com.sprint_sync_server.services.CommentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/comments")
class CommentController(private val commentService: CommentService) {
    @GetMapping("/{id}")
    fun getComment(@PathVariable id: String): ApiResponse<CommentResDto> {
        return when (val comment = commentService.getById(id)) {
            null -> ApiResponse(err = "Comment not found")
            else -> ApiResponse(commentService.getCommentResDto(comment))
        }
    }

    @GetMapping("/task/{id}")
    fun getCommentsOfTask(@PathVariable id: String): ApiResponse<List<CommentResDto>> {
        return when (val comments = commentService.getByTaskId(id)) {
            null -> ApiResponse(err = "Task not found")
            else -> ApiResponse(comments.map { commentService.getCommentResDto(it) })
        }
    }

    @PostMapping
    fun addComment(@RequestBody dto: CommentDto): ApiResponse<CommentResDto> {
        return when (commentService.existsById(dto.id)) {
            true -> ApiResponse(err = "Comment already exists")
            else -> ApiResponse(commentService.getCommentResDto(commentService.save(dto.toDomain())))
        }
    }

    @PostMapping("/task/{id}")
    fun addCommentToTask(
        @RequestHeader("Fire-id") uid: String,
        @RequestBody dto: CommentDto,
        @PathVariable id: String
    ): ApiResponse<CommentResDto> {
        return when (commentService.existsById(dto.id)) {
            true -> ApiResponse(err = "Comment already exists")
            else -> {
                try {
                    ApiResponse(commentService.addCommentToTask(uid, dto, id))
                } catch (e: Exception) {
                    return ApiResponse(err = e.message)
                }
            }
        }
    }

    @PatchMapping
    fun updateComment(@RequestBody dto: CommentDto): ApiResponse<CommentResDto> {
        return when (commentService.existsById(dto.id)) {
            true -> ApiResponse(commentService.getCommentResDto(commentService.save(dto.toDomain())))
            else -> ApiResponse(err = "Comment not found")
        }
    }

    @DeleteMapping("/{id}")
    fun deleteComment(@PathVariable id: String): ApiResponse<String> {
        return when (commentService.existsById(id)) {
            true -> {
                commentService.deleteById(id)
                ApiResponse("Comment deleted")
            }

            else -> ApiResponse(err = "Comment not found")
        }
    }
}