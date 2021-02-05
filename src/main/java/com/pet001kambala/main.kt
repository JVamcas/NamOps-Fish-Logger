package com.pet001kambala

import antlr.StringUtils
import com.google.common.base.CharMatcher
import java.util.regex.Pattern
import java.util.stream.Collectors


fun main() {
    val pattern: Pattern = Pattern.compile("([+]0\\d+)")

    val data = "\u0002+000004 kg\n"
//    if (data.contains("kg")) {
//        val dirtyWeight = data.split("kg").firstOrNull()
//        val weight = (dirtyWeight?.let { it.filter { it.isDigit() } } ?: "0").toInt()
//    }
//    val weight = data.filter { it.isDigit() }
//    println("new data is: $weight")

    val matcher = pattern.matcher(data)
    if (matcher.matches()) {
        println("data: ${matcher.group(1)}")
    } else println("No matches")
}
