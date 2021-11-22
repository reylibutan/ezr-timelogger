package me.reylibutan.ezr.timelogger.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.reflect.KClass

/**
 * Reads a JSON file with the [fileName]
 * It is assumed that the file contains an object with String keys and object values of the type [T]
 * Especially useful for semi-static lists. e.g. mapping files
 */
fun <T : Any> readMappingFile(fileName: String, clazz: KClass<T>): Map<String, T> {
  var map = emptyMap<String, T>()
  val mappingFile = Any::class.java.getResource(fileName)
  if (mappingFile != null) {
    map = Gson().fromJson(mappingFile.readText(), object : TypeToken<HashMap<String, T>>() {}.type)
  }

  return map
}
