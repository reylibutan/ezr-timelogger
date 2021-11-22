package me.reylibutan.ezr.timelogger.service

import me.reylibutan.ezr.timelogger.dto.TimeEntryApiRequestDto
import me.reylibutan.ezr.timelogger.dto.TimeEntryDto

class TimeEntryService {

  fun submitTimeEntry() {
    // TODO: allow this API-key to be configurable
    val apiKey = "5a759a7c243ead471351375594ec5653fd1d9f1b"
    val te = TimeEntryApiRequestDto("zara", 9131024, "2021-11-25", TimeEntryDto("0.1", 9, "hohoho"))

//  val result = "https://support.zodiacportsolutions.com/time_entries.json".httpGet().appendHeader("X-Redmine-API-Key", apiKey).responseString()
//  println(result)
  }
}