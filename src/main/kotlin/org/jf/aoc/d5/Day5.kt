package org.jf.aoc.d5

import org.jf.aoc.utils.readFile

fun main() {
    val input = "d5.txt".readFile()
    oneStar(input)
    twoStar(input)
}

fun oneStar(input: String) {
    val inputParts = input.split("\r\n\r\n")
    val ordering =
        inputParts[0].split("\r\n").map { s -> s.split("|").let { ordering -> Pair(ordering[0], ordering[1]) } }
            .toList()
            .sortedBy { Integer.parseInt(it.first) }
    val updates = inputParts[1].split("\r\n").map { s -> s.split(",").toList() }

    val result = updates.asSequence()
        .filter { u ->
            u.zipWithNext().all { it.first.isBefore(it.second, ordering) }
        }.map { u -> Integer.parseInt(u.middle()) }
        .reduce(Integer::sum)
    println(result)
}

fun twoStar(input: String) {
    val inputParts = input.split("\r\n\r\n")
    val ordering =
        inputParts[0].split("\r\n").map { s -> s.split("|").let { ordering -> Pair(ordering[0], ordering[1]) } }
            .toList()
            .sortedBy { Integer.parseInt(it.first) }
    val updates = inputParts[1].split("\r\n").map { s -> s.split(",").toList() }

    val result = updates.asSequence()
        .filter { u ->
            u.zipWithNext().any { !it.first.isBefore(it.second, ordering) }
        }.toList()
        .map { Integer.parseInt(it.order(ordering).middle()) }
        .reduce(Integer::sum)
    println(result)
}

fun List<String>.isOrdered(ordering: List<Pair<String, String>>): Boolean {
    return this.zipWithNext().all { it.first.isBefore(it.second, ordering) }
}

fun String.isBefore(other: String, ordering: List<Pair<String, String>>): Boolean {
    return ordering.count { this == it.second && other == it.first } == 0
}

fun <T> List<T>.middle(): T {
    return this[this.size / 2]
}

fun List<String>.order(ordering: List<Pair<String, String>>): List<String> {
    if (this.isOrdered(ordering)) {
        return this
    } else {
        val toSwitch = this.zipWithNext().first { p -> !p.first.isBefore(p.second, ordering) }
        return this.map { if (it == toSwitch.first) toSwitch.second else if (it == toSwitch.second) toSwitch.first else it }
            .order(ordering)
    }
}