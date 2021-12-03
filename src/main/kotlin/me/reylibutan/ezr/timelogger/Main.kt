package me.reylibutan.ezr.timelogger

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import me.reylibutan.ezr.timelogger.service.DsrService
import me.reylibutan.ezr.timelogger.service.TimeEntryService

fun main() {
  val csvLines = DsrService().dsrToCsv("D:\\___\\_lol\\ezr-timelogger\\src\\main\\resources\\timesheet_nov_w5_2021.dsr")
//  val csvLines = DsrService().dsrToCsv("/Users/reylibutan/IdeaProjects/ezr-timelogger/src/main/resources/timesheet_nov_w1_2021.dsr")
  val csvEntries = csvReader().readAllWithHeader(csvLines.joinToString("\n"))
  TimeEntryService().submitTimeEntries(csvEntries)
}