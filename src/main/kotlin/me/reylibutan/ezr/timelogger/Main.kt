package me.reylibutan.ezr.timelogger

import me.reylibutan.ezr.timelogger.dto.ActivityDto
import me.reylibutan.ezr.timelogger.service.TimeEntryService
import me.reylibutan.ezr.timelogger.util.readMappingFile

fun main() {
  val activityMap = readMappingFile("/activities.json", ActivityDto::class)
  val teSvc: TimeEntryService = TimeEntryService()

  teSvc.submitTimeEntry("/Users/reylibutan/src/timesheet.csv")
}