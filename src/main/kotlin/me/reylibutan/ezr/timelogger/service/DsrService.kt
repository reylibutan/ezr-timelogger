package me.reylibutan.ezr.timelogger.service

import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DsrService {

  fun normalizeDsr(fullPath: String) {
    val dsrFile = File(fullPath)
    if (!dsrFile.exists()) error("DSR file not found. (filePath = $fullPath)")

    val outputLines = mutableListOf<String>()
    var currDate: LocalDate? = null
    dsrFile.forEachLine {
      // skippable lines
      if (isSkip(it)) {
        outputLines.add(it)
        return@forEachLine
      }

      // date header
      val isDateHeader: Pair<Boolean, LocalDate> = isDateHeader(it)
      if (isDateHeader.first) {
        currDate = isDateHeader.second
        outputLines.add(it)
        return@forEachLine
      }

      // entries
      if (isNormalEntry(it)) {
        // TODO: parse line
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
    return !(loweredLine.contains("xx:xx") || loweredLine.startsWith("-") || loweredLine.startsWith("+"))
  }
}
