package me.reylibutan.ezr.timelogger.dto

data class TimeEntryDto(
  var hours: String,
  var activityId: Int?,
  var comments: String?
)
