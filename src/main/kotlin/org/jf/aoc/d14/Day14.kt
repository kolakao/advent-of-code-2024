package org.jf.aoc.d14

import org.jf.aoc.utils.readFile
import java.util.stream.IntStream
import kotlin.math.abs

fun main() {
    val input = "d14.txt".readFile()
    firstStar(input)
    secondStar(input)
}

fun firstStar(input: String) {
    val horizontalLimit = 101
    val verticalLimit = 103
    val secondsElapsed = 100
    val afterTimeElapsed = robotsAfterTimeElapsed(input, secondsElapsed, horizontalLimit, verticalLimit)

    println(afterTimeElapsed.count { r -> r.x < (horizontalLimit / 2) && r.y < (verticalLimit / 2) } *
            afterTimeElapsed.count { r -> r.x < (horizontalLimit / 2) && r.y > (verticalLimit / 2) } *
            afterTimeElapsed.count { r -> r.x > (horizontalLimit / 2) && r.y < (verticalLimit / 2) } *
            afterTimeElapsed.count { r -> r.x > (horizontalLimit / 2) && r.y > (verticalLimit / 2) })
}

private fun robotsAfterTimeElapsed(
    input: String,
    secondsElapsed: Int,
    horizontalLimit: Int,
    verticalLimit: Int
): List<Robot> {
    val robots = input.lines().map { l ->
        val (p, v) = l.split(" ")
        val (x, y) = p.substringAfter("=").split(",").map(Integer::parseInt)
        val (vx, vy) = v.substringAfter("=").split(",").map(Integer::parseInt)
        Robot(x, y, vx, vy)
    }

    val afterTimeElapsed = robots.map { r ->
        val newXRaw = (r.x + secondsElapsed * r.vx)
        val newYRaw = (r.y + secondsElapsed * r.vy)
        val newX =
            if (newXRaw >= 0) newXRaw % horizontalLimit else (horizontalLimit - (abs(newXRaw) % horizontalLimit)) % horizontalLimit
        val newY =
            if (newYRaw >= 0) newYRaw % verticalLimit else (verticalLimit - (abs(newYRaw) % verticalLimit)) % verticalLimit

        Robot(
            newX,
            newY,
            r.vx,
            r.vy
        )
    }
    return afterTimeElapsed
}

fun secondStar(input: String) {
    val horizontalLimit = 101
    val verticalLimit = 103
    IntStream.range(0, 100000).parallel().forEach { i ->
        val afterTimeElapsed = robotsAfterTimeElapsed(input, i, horizontalLimit, verticalLimit)
        val groupBy = afterTimeElapsed.groupBy { it.y }
        if (groupBy.values.any { it.distinctBy { dd -> dd.x }.count() > 30 }) {
            groupBy.entries.sortedBy { it.key }.forEach { e ->
                val sb = StringBuilder()
                for (j in 0..<horizontalLimit) {
                    if (e.value.any { it.x == j }) {
                        sb.append("X")
                    } else {
                        sb.append(".")
                    }
                }
                println(sb.toString())
            }
            println("VVVV ${i} VVVV")
            println()
        }
    }

}


data class Robot(val x: Int, val y: Int, val vx: Int, val vy: Int)