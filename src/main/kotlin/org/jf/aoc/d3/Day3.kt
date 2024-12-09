package org.jf.aoc.d3

import org.jf.aoc.utils.readFile
import java.util.*

fun main() {
    val input = "d3.txt".readFile()
    oneStar(input)
    twoStar(input)
}

fun oneStar(input: String) {
    val regex = "mul\\(\\d+,\\d+\\)".toRegex()
    val matches = regex.findAll(input).map { it.value }.toList()
    val result = matches
        .asSequence()
        .map {
            it.multiply()
        }
        .reduce(Integer::sum)
    println(result)
}

fun twoStar(input: String) {
    val regex = "mul\\(\\d+,\\d+\\)".toRegex()
    val doRegex = "do\\(\\)".toRegex()
    val dontRegex = "don't\\(\\)".toRegex()
    val matches = regex.findAll(input).map { Pair(it.range.first, it.value) }.toList()
    val dosMatches = doRegex.findAll(input).map { it.range.first }.toList()
    val dontsMatches = dontRegex.findAll(input).map { it.range.first }.plus(input.length).toList()

    val excluded = dontsMatches.zipWithNext().map { p ->
        Optional.ofNullable(dosMatches.firstOrNull { it in p.first..p.second })
            .map { IntRange(p.first, it) }
            .orElse(IntRange(p.first, p.second))
    }.toList()


    val result = matches.filter { mul -> excluded.count { it.contains(mul.first) } <= 0 }
        .map { it.second.multiply() }
        .reduce(Integer::sum)
    println()
    println(result)
}

fun String.multiply(): Int {
    val split = this.split(",")
    return Integer.parseInt(split[0].substringAfter("(")) * Integer.parseInt(split[1].substringBefore(")"))
}