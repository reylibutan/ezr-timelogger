package me.reylibutan.ezr.timelogger

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.reylibutan.ezr.timelogger.dto.Activity
import me.reylibutan.ezr.timelogger.dto.TimeEntry
import me.reylibutan.ezr.timelogger.dto.TimeEntryApiRequest


class Main

fun main() {
  // TODO: allow this API-key to be configurable
  val apiKey = "5a759a7c243ead471351375594ec5653fd1d9f1b"
  val te = TimeEntryApiRequest("zara", 9131024, "2021-11-25", TimeEntry("0.1", 9, "hohoho"))

//  val result = "https://support.zodiacportsolutions.com/time_entries.json".httpGet().appendHeader("X-Redmine-API-Key", apiKey).responseString()
//  println(result)

  val activityJson = Main::class.java.getResource("/activities.json")
  if (activityJson != null) {
    val activities = GsonBuilder().create().fromJson<Map<String, Activity>>(activityJson.readText(), object:TypeToken<HashMap<String, Activity>>(){}.type)
    println(activities)
  }
}