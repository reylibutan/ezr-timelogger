package me.reylibutan.ezr.timelogger.dto

data class TimeEntryDto(
  var hours: String,
  var comments: String?,
  var activityId: Int?
)
