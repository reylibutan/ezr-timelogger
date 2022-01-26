package me.reylibutan.ezr.timelogger.service

import com.google.gson.internal.LinkedTreeMap
import me.reylibutan.ezr.timelogger.util.readMappingFile
import java.io.File
import java.lang.Exception
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DsrService {
  private val varTimeMarker: String = "xx:xx"
  private val activityMap = readMappingFile("/activities.json", LinkedTreeMap::class)

  fun dsrToCsv(csvPath: String): List<String> {
    val dsrFile = File(csvPath)
    if (!dsrFile.exists()) error("DSR file not found. (filePath = $csvPath)")

    val inputLines = dsrFile.readLines()
    val normalizedLines = normalizeDsr(inputLines)

    val csvRows: MutableList<String> = mutableListOf()
    csvRows.add("projectId,issueId,spentOn,hours,comments,activityId")
    csvRows.addAll(convertDsrToTimeEntries(normalizedLines))

    return csvRows
  }

  // normalizing just means converting "-" and "+" lines/entries
  // this is considered as the first pass
  private fun normalizeDsr(lines: List<String>): List<String> {
    var currStartTime: String?
    var currEndTime: String?
    val outputLines = mutableListOf<String>()

    for ((index, it) in lines.withIndex()) {
      var line = it
      // skippable lines
      if (isSkip(line)) {
        outputLines.add(line)
        continue
      }

      // date header
      val isDateHeader: Pair<Boolean, LocalDate> = isDateHeader(line)
      if (isDateHeader.first) {
        outputLines.add(line)
        continue
      }

      currStartTime = getStartTime(line)
      currEndTime = getEndTime(line)

      // entries
      // add issue ID
      if (isNormalEntry(line)) {
        outputLines.add(line)
      } else {
        // parse xx
        if (currStartTime?.isNotBlank() == true && currEndTime.equals(varTimeMarker, true)) {
          val deducedEndTime = deduceEndTime(lines, index + 1, currStartTime)
          line = line.replace(varTimeMarker, deducedEndTime)
        }

        // parse -
        // TODO: later

        // parse +
        outputLines.add(line)
      }
    }

    return outputLines
  }

  private fun isSkip(line: String): Boolean = line.startsWith("--") || line.isBlank()

  private fun isDateHeader(line: String): Pair<Boolean, LocalDate> {
    val defaultDateFormat = "MMMM d, yyyy"
    val dateTokenStart = "["
    val dateTokenEnd = "]"

    if (line.startsWith(dateTokenStart) && line.contains(dateTokenEnd)) {
      val dateStr = line.substring(1, line.indexOf(dateTokenEnd))
      return Pair(true, LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(defaultDateFormat)))
    }

    return Pair(false, LocalDate.now())
  }

  // normal entry = a line with start and end times explicitly defined
  private fun isNormalEntry(line: String): Boolean {
    val loweredLine = line.lowercase()
    return !(loweredLine.contains(varTimeMarker) || loweredLine.startsWith("-") || loweredLine.startsWith("+") || loweredLine.isBlank())
  }

  private fun getStartTime(line: String): String? {
    if (line.length < 5) return null

    val possibleStartTime = line.substring(0, 5)
    return if (possibleStartTime.matches("\\d{2}:\\d{2}".toRegex())) possibleStartTime else null
  }

  private fun getEndTime(line: String): String? {
    return if (line.length >= 13) line.substring(8, 13) else null
  }

  private fun timeStringToLocalTime(timeStr: String): LocalTime {
    val time = LocalTime.parse(timeStr)

    // I never log time before 8am, so anything of the format 02:00, means I meant 14:00, sorry lol
    return if (time.isBefore(LocalTime.of(8, 0))) time.plusHours(12) else time
  }

  private fun deduceEndTime(lines: List<String>, startingLineIndex: Int, currStartTime: String): String {
    // we assume it is the last task of the day, so most probably 6:00
    var defaultEndTime = "06:00"

    if (startingLineIndex >= lines.size) return defaultEndTime

    val noonTime = LocalTime.of(12, 0)
    val startTime = timeStringToLocalTime(currStartTime)
    if (startTime.isBefore(noonTime)) {
      defaultEndTime = "12:00"
    }

    for (i in startingLineIndex until lines.size) {
      if (isDateHeader(lines[i]).first) return defaultEndTime

      val deducedEndTime = getStartTime(lines[i])
      if (deducedEndTime != null) {
        // maybe the end time should be 12:00 noon
        val endTime = timeStringToLocalTime(deducedEndTime)

        return if (startTime.isBefore(noonTime) && endTime.isAfter(noonTime)) noonTime.toString() else deducedEndTime
      }
    }

    return defaultEndTime
  }

  // we assume that the lines fed here are "normalized"
  // this is considered as the second pass
  private fun convertDsrToTimeEntries(dsr: List<String>): List<String> {
    val entries = mutableListOf<String>()

    var currDate: LocalDate? = null
    for ((index, l) in dsr.withIndex()) {
      try {
        if (isSkip(l)) continue

        // deduce current date
        val isDateHeader: Pair<Boolean, LocalDate> = isDateHeader(l)
        if (isDateHeader.first) {
          currDate = isDateHeader.second
          continue
        }

        // deduce hours
        val hoursPair = trimDuration(l)
        val hours = computeHours(hoursPair.first)

        // deduce issue id
        val issueId = deduceIssueId(hoursPair.second)

        // deduce project id based on issue id
        val projectId = deduceProjectId(issueId)
        val comments = hoursPair.second.replace(issueId.toString(), "").trim()

        // deduce activity id
        val activityId = deduceActivityId(l)

        entries.add("$projectId,${issueId ?: ""},$currDate,$hours,\"$comments\",$activityId")
      } catch (e: Exception) {
        println("ERROR on line:${index + 1} >>> $l")
        e.printStackTrace()
      }
    }

    return entries
  }

  // returns a Pair<hoursPart, remainingPart>
  private fun trimDuration(l: String): Pair<String, String> {
    var pair = Pair("", l)

    if (l.startsWith("-") || l.startsWith("+")) {
      pair = Pair(l.substring(0, 5), l.substring(8))
    } else {
      val starTime = getStartTime(l)
      val endTime = getEndTime(l)

      if (!(starTime == null || endTime == null)) {
        pair = Pair(l.substring(0, 13), l.substring(16))
      }
    }

    return pair
  }

  private fun computeHours(duration: String): Float {
    var hours = 0f

    if (duration.startsWith("-") || duration.startsWith("+")) {
      hours = duration.substring(1).toFloat()
    } else {
      val starTime = getStartTime(duration)
      val endTime = getEndTime(duration)

      if (!(starTime == null || endTime == null)) {
        val sTime = timeStringToLocalTime(starTime)
        val eTime = timeStringToLocalTime(endTime)
        hours = (Duration.between(sTime, eTime).toMinutes().toFloat() / 60)
      }
    }

    return hours
  }

  private fun deduceIssueId(line: String): Int? {
    // scrum
    if (line.startsWith("STS Scrum", true)) return null

    // sick leave
    if (line.startsWith("SL", true) || line.contains("sick leave", true)) return 29238

    // COM
    if (line.startsWith("COM", true)) return null

    // normal
    val possibleIssueId = "^\\d{5,} ".toRegex().find(line)
    if (possibleIssueId != null) {
      return possibleIssueId.value.trim().toInt()
    }

    return null
  }

  private fun deduceProjectId(issueId: Int?): String {
    if (issueId == 29238) return "leave"

    return "zara"
  }

  private fun deduceActivityId(line: String): Any {
    when {
      line.contains("COM", true) -> return getActivityIdByKey("meeting")
      line.contains("STS", true) -> return getActivityIdByKey("meeting")
      line.contains("SL", true) -> return getActivityIdByKey("leave")
      line.contains("sick", true) -> return getActivityIdByKey("leave")
      line.contains("leave", true) -> return getActivityIdByKey("leave")
      line.contains("test", true) -> return getActivityIdByKey("testing")
      line.contains("cpk", true) -> return getActivityIdByKey("development")
      line.contains("fix ", true) -> return getActivityIdByKey("development")
      line.contains("check ", true) -> return getActivityIdByKey("support")
      line.contains("support ", true) -> return getActivityIdByKey("support")
      line.contains("review ", true) -> return getActivityIdByKey("review")
    }

    return getActivityIdByKey("development")
  }

  private fun getActivityIdByKey(key: String): Int {
    return activityMap[key]!!["id"]!!.toString().toFloat().toInt()
  }
}
