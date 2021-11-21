package me.reylibutan.ezr.timelogger.dto

class TimeEntryApiRequest(
  var projectId: String,
  var issueId: Int?,
  var spentOn: String,
  var timeEntry: TimeEntry
)