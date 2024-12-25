package org.jf.aoc.d13

import org.jf.aoc.utils.readFile
import java.math.BigDecimal

fun main() {
    val input = "d13.txt".readFile()
    firstStar(input)
    secondStar(input)
}

fun firstStar(input: String) {
    val dd = input
        .lines()
        .filter { s -> s.isNotBlank() }
        .chunked(3)
        .map { l ->
            val (a1, a2) = l[0].substringAfter(':').split(",")
                .map { s -> BigDecimal(s.filter { c -> c.isDigit() }) }
            val (b1, b2) = l[1].substringAfter(':').split(",")
                .map { s -> BigDecimal(s.filter { c -> c.isDigit() }) }
            val (c1, c2) = l[2].substringAfter(':').split(",")
                .map { s -> BigDecimal(s.filter { c -> c.isDigit() }) }
            solveEquationsSystem(a1, b1, c1, a2, b2, c2)
        }
        .map { if (it == null) BigDecimal.ZERO else (it.first.multiply(BigDecimal(3)) + it.second) }

    println((dd.reduce { bd1, bd2 -> bd1.add(bd2) }))
}

fun secondStar(input: String) {
    val dd = input
        .lines()
        .filter { s -> s.isNotBlank() }
        .chunked(3)
        .map { l ->
            val (a1, a2) = l[0].substringAfter(':').split(",")
                .map { s -> BigDecimal(s.filter { c -> c.isDigit() }) }
            val (b1, b2) = l[1].substringAfter(':').split(",")
                .map { s -> BigDecimal(s.filter { c -> c.isDigit() }) }
            val (c1, c2) = l[2].substringAfter(':').split(",")
                .map { s -> BigDecimal(s.filter { c -> c.isDigit() }) }
            solveEquationsSystem(a1, b1, c1.add(BigDecimal(10000000000000)), a2, b2, c2.add(BigDecimal(10000000000000)))
        }
        .map { if (it == null) BigDecimal.ZERO else (it.first.multiply(BigDecimal(3)) + it.second) }

    println((dd.reduce { bd1, bd2 -> bd1.add(bd2) }))
}

fun solveEquationsSystem(
    a1: BigDecimal,
    b1: BigDecimal,
    c1: BigDecimal,
    a2: BigDecimal,
    b2: BigDecimal,
    c2: BigDecimal
): Pair<BigDecimal, BigDecimal>? {
    val determinant = a1 * b2 - a2 * b1
    val determinantX = c1 * b2 - c2 * b1
    val determinantY = a1 * c2 - a2 * c1

    if (determinant != BigDecimal.ZERO) {
        val x = determinantX / determinant
        val y = determinantY / determinant
        if (determinantX % determinant == BigDecimal.ZERO && determinantY % determinant == BigDecimal.ZERO) {
            return Pair(x, y)
        }
    }
    return null
}