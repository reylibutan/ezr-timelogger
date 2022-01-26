package me.reylibutan.ezr.timelogger

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import me.reylibutan.ezr.timelogger.service.DsrService
import me.reylibutan.ezr.timelogger.service.TimeEntryService

fun main() {
  val csvLines = DsrService().dsrToCsv("src/main/resources/2022_01/timesheet_jan_w3_2022.dsr")
  val csvEntries = csvReader().readAllWithHeader(csvLines.joinToString("\n"))
  TimeEntryService().previewTimeEntries(csvEntries)
}