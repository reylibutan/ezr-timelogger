package me.reylibutan.ezr.timelogger.service

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import me.reylibutan.ezr.timelogger.dto.TimeEntryApiRequestDto
import me.reylibutan.ezr.timelogger.dto.TimeEntryDto
import java.io.File

class TimeEntryService {
  // TODO: allow this API-key to be configurable
  val apiKey = "5a759a7c243ead471351375594ec5653fd1d9f1b"
  val redmineApiPrefix = "https://support.zodiacportsolutions.com/"
  //  val result = "https://support.zodiacportsolutions.com/time_entries.json".httpGet().appendHeader("X-Redmine-API-Key", apiKey).responseString()

  fun submitTimeEntry(csvFullPath: String) {



  }

  fun getTimeEntriesFromCsv(csvFullPath: String) {
    val rows: List<Map<String, String>> = csvReader().readAllWithHeader(File(csvFullPath))
    if (rows.isEmpty()) {
      return
    }

    for (row in rows) {
//      val csvProjectId = row["projectId"].orEmpty()
//      val csvIssueId = row["issueId"].orEmpty()
//      val csvSpentOn = row["spentOn"].orEmpty()
//      val csvHours = row["hours"].orEmpty()
//      val csvComments = row["comments"].orEmpty()
//      val csvActivityId = row["activityId"].orEmpty()
//
//      // some data transformation
//      val issueId = csvIssueId.ifBlank { -1 }
//
//      // TODO: to row by row validation here, possibly log if row is problematic, and move on to next row
//
//      val teRequestDto = TimeEntryApiRequestDto(projectId, issueId, spentOn, TimeEntryDto(hours, comments, activityId))
    }
  }
}