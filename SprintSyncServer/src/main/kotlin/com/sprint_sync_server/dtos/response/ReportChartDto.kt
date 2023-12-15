package com.sprint_sync_server.dtos.response

data class ReportChartDto(
    val id: String? = null,
    val sprintNumber: Int,
    val listOfTask: List<List<TaskResDto>>
)
