package me.reylibutan.ezr.timelogger.dto

class TimeEntryApiRequestDto(
  var projectId: String,
  var issueId: Int?,
  var spentOn: String,
  var timeEntry: TimeEntryDto
)