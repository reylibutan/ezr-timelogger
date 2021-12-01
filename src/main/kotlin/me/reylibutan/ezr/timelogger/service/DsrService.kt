package me.reylibutan.ezr.timelogger.service

import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DsrService {

  val varTimeMarker: String = "xx:xx"

  // normalizing just means converting "-" and "+" lines/entries
  fun normalizeDsr(fullPath: String) {
    val dsrFile = File(fullPath)
    if (!dsrFile.exists()) error("DSR file not found. (filePath = $fullPath)")

    var currDate: LocalDate? = null
    var currStartTime: String? = null
    var currEndTime: String? = null

    val outputLines = mutableListOf<String>()
    val inputLines: List<String> = dsrFile.readLines()
    for ((index, it) in inputLines.withIndex()) {
      var line = it
      // skippable lines
      if (isSkip(line)) {
        outputLines.add(line)
        continue
      }

      // date header
      val isDateHeader: Pair<Boolean, LocalDate> = isDateHeader(line)
      if (isDateHeader.first) {
        currDate = isDateHeader.second
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
          val deducedEndTime = deduceEndTime(inputLines, index + 1, currStartTime)
          line = line.replace(varTimeMarker, deducedEndTime)
        }

        // parse -
        // parse +
        outputLines.add(line)
      }
    }

    // TODO: output in a newly created file
    for (l in outputLines) {
      println(l)
    }
  }

  private fun isSkip(line: String): Boolean = line.startsWith("--")

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
    return if (line.length > 14) line.substring(8, 13) else null
  }

  private fun timeStringToLocalTime(timeStr: String): LocalTime {
    val time = LocalTime.parse(timeStr)

    // I never log time before 8am, so anything of the format 02:00, means I meant 14:00, sorry lol
    return if (time.isBefore(LocalTime.of(8, 0))) time.plusHours(12) else time
  }

  private fun deduceEndTime(lines: List<String>, startingLineIndex: Int, currStartTime: String): String {
    // we assume it is the last task of the day, so most probably 6:00
    val defaultEndTime = "06:00"

    if (startingLineIndex >= lines.size) return defaultEndTime

    for (i in startingLineIndex until lines.size) {
      if (isDateHeader(lines[i]).first) return defaultEndTime

      val deducedEndTime = getStartTime(lines[i])
      if (deducedEndTime != null) {
        // maybe the end time should be 12:00 noon
        val startTime = timeStringToLocalTime(currStartTime)
        val endTime = timeStringToLocalTime(deducedEndTime)

        val noonTime = LocalTime.of(12, 0)
        return if (startTime.isBefore(noonTime) && endTime.isAfter(noonTime)) noonTime.toString() else deducedEndTime
      }
    }

    return defaultEndTime
  }
}
