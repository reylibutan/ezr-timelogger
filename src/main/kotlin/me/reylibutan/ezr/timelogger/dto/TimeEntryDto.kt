package me.reylibutan.ezr.timelogger.dto

data class TimeEntryDto(val hours: String, val comments: String?, val activityId: Int?) {
  override fun toString(): String {
    return "TimeEntryDto(hours='$hours', comments=$comments, activityId=$activityId)"
  }
}
