package me.reylibutan.ezr.timelogger.dto

data class TimeEntryApiRequestDto(val projectId: String, val issueId: Int?, val spentOn: String, val timeEntry: TimeEntryDto) {
  override fun toString(): String {
    return "TimeEntryApiRequestDto(projectId='$projectId', issueId=$issueId, spentOn='$spentOn', timeEntry=$timeEntry)"
  }
}