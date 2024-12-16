package org.jf.aoc.d6

import org.jf.aoc.utils.readFile
import java.util.HashMap

internal typealias LabMap = List<List<PathPosition>>

fun main() {
    val input = "d6.txt".readFile()
    firstStar(input)
    secondStar(input)
}

fun firstStar(input: String) {
    val labMap = input.lines()
        .map { l -> l.toList() }
        .mapIndexed { i, l -> l.mapIndexed { j, c -> PathPosition(i, j, false, c) } }
    val guardPosition = labMap
        .flatten()
        .find { pp -> pp.symbol == '^' }!!
    val guard = Guard(guardPosition.x, guardPosition.y, labMap)
    var guardOut = false
    while (!guardOut) {
        labMap[guard.x][guard.y].isInThePath = true
        guard.move()
        guardOut = guard.isOut()
    }

    val result = labMap.flatten().count { pp -> pp.isInThePath }
    println(result)

}

fun secondStar(input: String) {
    val labMap = input.lines()
        .map { l -> l.toList() }
        .mapIndexed { i, l -> l.mapIndexed { j, c -> PathPosition(i, j, false, c) } }
    val (initX, initY) = labMap
        .flatten()
        .find { pp -> pp.symbol == '^' }!!.let { Pair(it.x, it.y) }
    println(initX)
    println(initY)
    val result =
        labMap.flatten().map {
            val labMapWithNewObstacle = labMap.toMutableList()
            val rowWithNewObstacle = labMap[it.x].toMutableList()
            if (it.x != initX || it.y != initY) {
                rowWithNewObstacle[it.y] = it.copy(symbol = 'O')
            }
            labMapWithNewObstacle[it.x] = rowWithNewObstacle
            labMapWithNewObstacle.toList()
        }
            .map { possibleLabMap ->
                val guard = Guard(initX, initY, possibleLabMap)
                when (guard.isLooped()) {
                    true -> 1
                    else -> 0
                }
            }
            .reduce(Integer::sum)
    println(result)


}


internal class Guard(
    var x: Int,
    var y: Int,
    private val labMap: LabMap
) {
    private var direction = "UP"
    private val obstaclesVisited: HashMap<PathPosition, String> = HashMap()

    private fun turnRight() {
        direction = when (direction) {
            "UP" -> "RIGHT"
            "RIGHT" -> "DOWN"
            "DOWN" -> "LEFT"
            "LEFT" -> "UP"
            else -> throw Exception("REEE")
        }
    }

    fun move(): Boolean {
        val (x: Int, y: Int) = when (direction) {
            "UP" -> Pair(x - 1, y)
            "RIGHT" -> Pair(x, y + 1)
            "DOWN" -> Pair(x + 1, y)
            "LEFT" -> Pair(x, y - 1)
            else -> throw Exception("REEE")
        }
        if (!isOut(x, y) && labMap.isObstacle(x, y)) {
            if (obstaclesVisited[labMap[x][y]] == direction) {
                return true
            }
            obstaclesVisited[labMap[x][y]] = direction
            turnRight()
            return move()
        }
        this.x = x
        this.y = y
        return false
    }

    fun isOut(): Boolean {
        return isOut(x, y)
    }

    fun isLooped(): Boolean {
        while (!isOut()) {
            if (move()) {
                return true
            }
        }
        return false
    }

    private fun isOut(x: Int, y: Int): Boolean {
        return x < 0 || y < 0 || x >= labMap.size || y >= labMap[0].size
    }

}


internal data class PathPosition(val x: Int, val y: Int, var isInThePath: Boolean, val symbol: Char)

internal fun LabMap.isObstacle(x: Int, y: Int): Boolean {
    return this[x][y].symbol == '#' || this[x][y].symbol == 'O'
}

internal fun LabMap.print() {
    for (pathPositions in this) {
        pathPositions.map { p -> p.symbol }.forEach(::print)
        println()
    }
}