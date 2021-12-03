package me.reylibutan.ezr.timelogger.service

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
import com.google.gson.GsonBuilder
import me.reylibutan.ezr.timelogger.dto.TimeEntryApiRequestDto
import me.reylibutan.ezr.timelogger.dto.TimeEntryDto
import java.io.File

class TimeEntryService {
  // TODO: allow this API-key to be configurable
  private val apiKey = "5a759a7c243ead471351375594ec5653fd1d9f1b"
  private val redmineApiPrefix = "https://support.zodiacportsolutions.com/"

  fun previewTimeEntries(csvEntries: List<String>): Map<String, Float> {
    val timeEntries = csvEntriesToTimeEntries(csvReader().readAllWithHeader(File(csvFullPath)))
  }

  fun previewTimeEntries(csvFullPath: String): Map<String, Float> {
    val timeEntries = csvEntriesToTimeEntries(csvReader().readAllWithHeader(File(csvFullPath)))

    val hoursPerDateMap: MutableMap<String, Float> = mutableMapOf()
    for (te in timeEntries) {
      val date = te.spentOn

      // create key for the first time
      if (!hoursPerDateMap.contains(date)) hoursPerDateMap[date] = 0f

      // accumulate all hours for each date; this is helpful when checking which dates don't have 8 hours of time entries
      hoursPerDateMap[date] = hoursPerDateMap[date]?.plus(te.timeEntry.hours.toFloat()) ?: 0f
    }

    return hoursPerDateMap
  }

  fun submitTimeEntries(csvFullPath: String) {
    val teRequests = csvEntriesToTimeEntries(csvFullPath)

    val gson = GsonBuilder().setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create()
    for (teRequest in teRequests) {
//      val (request, response, result) = ("${redmineApiPrefix}time_entries.json").httpPost()
//        .appendHeader("X-Redmine-API-Key", apiKey)
//        .appendHeader("Content-Type", "application/json")
//        .body(gson.toJson(teRequest)).response()

      // TODO: proper result handling, generalize POST request to HttpUtil
      println(teRequest)
    }
  }

  private fun csvEntriesToTimeEntries(csvRows: List<Map<String, String>>): List<TimeEntryApiRequestDto> {
    val teRequests: MutableList<TimeEntryApiRequestDto> = mutableListOf()
    for ((index, row) in csvRows.withIndex()) {
      val projectId = row["projectId"]
      if (projectId.isNullOrEmpty()) {
        println("Skipped line ${index + 2}. Project ID cannot be blank")
        continue
      }

      val issueId = row["issueId"]
      if (issueId.isNullOrEmpty()) {
        println("Skipped line ${index + 2}. Issue ID cannot be blank")
        continue
      }

      val spentOn = row["spentOn"]
      if (spentOn.isNullOrEmpty()) {
        println("Skipped line ${index + 2}. Spent on cannot be blank")
        continue
      }

      val hours = row["hours"]
      if (hours.isNullOrEmpty()) {
        println("Skipped line ${index + 2}. Hours cannot be blank")
        continue
      }

      val activityId = row["activityId"]
      if (activityId.isNullOrEmpty()) {
        println("Skipped line ${index + 2}. Activity ID cannot be blank")
        continue
      }

      val comments = row["comments"]

      teRequests.add(
        TimeEntryApiRequestDto(
          projectId,
          issueId.toInt(),
          spentOn,
          TimeEntryDto(hours, comments, activityId.toInt())
        )
      )
    }

    return teRequests
  }
}