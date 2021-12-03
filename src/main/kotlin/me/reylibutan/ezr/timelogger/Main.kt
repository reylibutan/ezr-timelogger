package me.reylibutan.ezr.timelogger

import me.reylibutan.ezr.timelogger.service.DsrService

fun main() {
//  val activityMap = readMappingFile("/activities.json", ActivityDto::class)
//  val teSvc: TimeEntryService = TimeEntryService()

//  teSvc.submitTimeEntry("D:\\___\\timesheet.csv")
//  teSvc.submitTimeEntries("/Users/reylibutan/src/timesheet.csv")
//  val hoursPerDate: Map<String, Float> = teSvc.previewTimeEntries("/Users/reylibutan/src/timesheet.csv")
//  val hoursPerDate: Map<String, Float> = teSvc.previewTimeEntries("D:\\___\\timesheet.csv")
//  for (date in hoursPerDate.keys) {
//    println("<$date> - ${hoursPerDate[date]}")
//  }
//  val csvLines = DsrService().dsrToCsv("D:\\___\\_lol\\ezr-timelogger\\src\\main\\resources\\timesheet_nov_2021.dsr")
  val csvLines = DsrService().dsrToCsv("/Users/reylibutan/IdeaProjects/ezr-timelogger/src/main/resources/timesheet_nov_2021.dsr")

}