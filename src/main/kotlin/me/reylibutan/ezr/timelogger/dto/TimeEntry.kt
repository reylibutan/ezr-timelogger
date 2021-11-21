package me.reylibutan.ezr.timelogger.dto

data class TimeEntry(
  var hours: String,
  var activityId: Int?,
  var comments: String?
)
