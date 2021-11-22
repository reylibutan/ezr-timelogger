package me.reylibutan.ezr.timelogger

import me.reylibutan.ezr.timelogger.dto.ActivityDto
import me.reylibutan.ezr.timelogger.util.readMappingFile

fun main() {
  val map = readMappingFile("/activities.json", ActivityDto::class)
  println(map)
}