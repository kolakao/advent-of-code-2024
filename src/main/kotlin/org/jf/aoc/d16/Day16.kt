package org.jf.aoc.d16

import org.jf.aoc.utils.readFile
import java.util.HashMap

internal typealias TileMap = List<List<MapTile>>

val map: MutableMap<MapTile, Long> = HashMap()

var scored : List<Pair<Long,List<Pair<Int,Int>>>> = emptyList()

fun main() {
    val input = "d16.txt".readFile()
    val tileMap = input.lines().mapIndexed { i, line ->
        line.mapIndexed { j, char ->
            MapTile(j, i, char)
        }
    }
    firstStar(tileMap)
    secondStar(tileMap)
}

fun firstStar(tileMap: TileMap) {
    val result = tileMap.moveIterative( Direction.EAST, 0, emptyList())
    println(result)
}

private data class State(
    val mapTile: MapTile,
    val direction: Direction,
    val score: Long,
    val visited: List<Pair<Int, Int>>
)

private fun TileMap.moveIterative(
    startDirection: Direction,
    startScore: Long,
    startVisited: List<Pair<Int, Int>>,
): Long {
    val startTile = this.findStart()
    val stack = mutableListOf(State(startTile, startDirection, startScore, startVisited))
    var bestScore = 0L
    while (stack.isNotEmpty()) {

        val state = stack.removeAt(stack.size - 1)
        val (mapTile, direction, score, visited) = state

        if (mapTile.isWall()) {
            continue
        }
        if (visited.contains(Pair(mapTile.x, mapTile.y))) {
            continue
        }
        if(map[mapTile] != null && map[mapTile]!! < score-1000) {
            continue
        }
        map[mapTile] = score
        val visitedNew = visited + Pair(mapTile.x, mapTile.y)
        if (mapTile.tileType == 'E') {
            println("Found end at $score")
            if (score <= bestScore || bestScore == 0L) {
                bestScore = score
                scored = scored + Pair(score, visitedNew)
            }
            continue
        }
        if (bestScore != 0L && score >= bestScore) {
            continue
        }


        val nextMoves = when (direction) {
            Direction.EAST -> {
                val nextTile = this[mapTile.y][mapTile.x + 1]
                val tileToTheRight = this[mapTile.y + 1][mapTile.x]
                val tileToTheLeft = this[mapTile.y - 1][mapTile.x]
                if (nextTile.isWall() && tileToTheRight.isWall() && tileToTheLeft.isWall()) {
                    emptyList()
                } else {
                    listOf(
                        Triple(nextTile, direction, score + 1),
                        Triple(tileToTheRight, Direction.SOUTH, score + 1001),
                        Triple(tileToTheLeft, Direction.NORTH, score + 1001)
                    ).filter { t -> !t.first.isWall() }
                }
            }

            Direction.NORTH -> {
                val nextTile = this[mapTile.y - 1][mapTile.x]
                val tileToTheRight = this[mapTile.y][mapTile.x + 1]
                val tileToTheLeft = this[mapTile.y][mapTile.x - 1]
                if (nextTile.isWall() && tileToTheRight.isWall() && tileToTheLeft.isWall()) {
                    emptyList()
                } else {
                    listOf(
                        Triple(nextTile, direction, score + 1),
                        Triple(tileToTheRight, Direction.EAST, score + 1001),
                        Triple(tileToTheLeft, Direction.WEST, score + 1001)
                    ).filter { t -> !t.first.isWall() }
                }
            }

            Direction.SOUTH -> {
                val nextTile = this[mapTile.y + 1][mapTile.x]
                val tileToTheRight = this[mapTile.y][mapTile.x - 1]
                val tileToTheLeft = this[mapTile.y][mapTile.x + 1]
                if (nextTile.isWall() && tileToTheRight.isWall() && tileToTheLeft.isWall()) {
                    emptyList()
                } else {
                    listOf(
                        Triple(nextTile, direction, score + 1),
                        Triple(tileToTheRight, Direction.WEST, score + 1001),
                        Triple(tileToTheLeft, Direction.EAST, score + 1001)
                    ).filter { t -> !t.first.isWall() }
                }
            }

            Direction.WEST -> {
                val nextTile = this[mapTile.y][mapTile.x - 1]
                val tileToTheRight = this[mapTile.y - 1][mapTile.x]
                val tileToTheLeft = this[mapTile.y + 1][mapTile.x]
                if (nextTile.isWall() && tileToTheRight.isWall() && tileToTheLeft.isWall()) {
                    emptyList()
                } else {
                    listOf(
                        Triple(nextTile, direction, score + 1),
                        Triple(tileToTheRight, Direction.NORTH, score + 1001),
                        Triple(tileToTheLeft, Direction.SOUTH, score + 1001)
                    ).filter { t -> !t.first.isWall() }
                }
            }
        }

        for ((nextTile, nextDir, nextScore) in nextMoves) {
            stack.add(State(nextTile, nextDir, nextScore, visitedNew))
        }
    }

    return bestScore
}

fun secondStar(tileMap: TileMap) {
    val result = tileMap.moveIterative(Direction.EAST, 0, emptyList())
    val count = scored.filter {
        it.first == result
    }
        .map { it.second }
        .flatten()
        .toSet()
        .count()
    println(result)
    println(count)
}

private fun TileMap.findStart(): MapTile {
    return this.flatten().first {
        it.tileType == 'S'
    }
}

data class MapTile(val x: Int, val y: Int, val tileType: Char) {
    fun isWall(): Boolean {
        return tileType == '#'
    }
}

enum class Direction {
    NORTH, SOUTH, EAST, WEST
}