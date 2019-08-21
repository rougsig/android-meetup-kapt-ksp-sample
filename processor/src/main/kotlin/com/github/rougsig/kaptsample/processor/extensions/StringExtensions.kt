package com.github.rougsig.kaptsample.processor.extensions

internal fun String.beginWithUpperCase(): String {
  return when (length) {
    0 -> ""
    1 -> toUpperCase()
    else -> first().toUpperCase() + substring(1)
  }
}

internal fun String.toSnakeCase(): String {
  val builder = StringBuilder()
  var isFirst = true
  forEach {
    if (it.isUpperCase()) {
      if (isFirst) isFirst = false
      else builder.append("_")
      builder.append(it)
    } else {
      builder.append(it)
    }
  }
  return builder.toString().toUpperCase()
}
