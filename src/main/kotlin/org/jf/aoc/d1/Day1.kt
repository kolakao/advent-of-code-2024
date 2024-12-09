package org.jf.aoc.d1

import org.jf.aoc.utils.readFile
import kotlin.math.abs


fun main() {
    val input = "d1.txt".readFile()
    oneStar(input)
    twoStar(input)
}

fun twoStar(input: String) {
    val inputLists = input.lines()
        .map { line ->
            val values = line.split("\\s+".toRegex()).map { it.toInt() }
            Pair(values[0], values[1])
        }
        .unzip()
    val l1 = inputLists.first
    val l2 = inputLists.second
    val result = l1.map { v -> l2.count { it == v } * v }.reduce(Integer::sum)
    println(result)
}

fun oneStar(input: String) {
    val inputLists = input.lines()
        .map { line ->
            val values = line.split("\\s+".toRegex()).map { it.toInt() }
            Pair(values[0], values[1])
        }
        .unzip()
    val l1 = inputLists.first.sorted()
    val l2 = inputLists.second.sorted()
    val result = l1.zip(l2).map { abs(it.first - it.second) }.reduce(Integer::sum)
    println(result)
}