package me.reylibutan.ezr.timelogger

import com.github.kittinunf.fuel.httpGet
import me.reylibutan.ezr.timelogger.dto.TimeEntry
import me.reylibutan.ezr.timelogger.dto.TimeEntryApiRequest

fun main() {
  // TODO: allow this API-key to be configurable
  val apiKey = "5a759a7c243ead471351375594ec5653fd1d9f1b"
  val te = TimeEntryApiRequest("zara", 9131024, "2021-11-25", TimeEntry("0.1", 9, "hohoho"))

  val result = "https://support.zodiacportsolutions.com/time_entries.json".httpGet().appendHeader("X-Redmine-API-Key", apiKey).responseString()

  println(result)
}