package me.reylibutan.ezr.timelogger

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import me.reylibutan.ezr.timelogger.service.DsrService
import me.reylibutan.ezr.timelogger.service.TimeEntryService

fun main() {
  val csvLines = DsrService().dsrToCsv("src/main/resources/2022_04/timesheet_apr_w4_2022.dsr")
  val csvEntries = csvReader().readAllWithHeader(csvLines.joinToString("\n"))
  TimeEntryService().submitTimeEntries(csvEntries)
}