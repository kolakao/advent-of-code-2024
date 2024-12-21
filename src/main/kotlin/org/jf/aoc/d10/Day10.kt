package org.jf.aoc.d10

import org.jf.aoc.utils.readFile

fun main() {
    val input = "d10.txt".readFile()
    firstStar(input)
    secondStar(input)
}

fun firstStar(input: String) {
    val map = input.lines().mapIndexed { i, l -> l.mapIndexed { j, c -> PathPoint(i, j, c.digitToInt()) } }
    val groundPoints = map.flatten().filter { n -> n.height == 0 }
    val trails = groundPoints.map { p -> p.findTrails(map, listOf()) }
    val trailheads = trails.map { tl ->
        val setOfPossiblePeaks = mutableSetOf<PathPoint>()
        tl.forEach { t ->
            t.first { p -> p.height == 9 }.let { setOfPossiblePeaks.add(it) }
        }
        setOfPossiblePeaks
    }

    println(trailheads.map(MutableSet<PathPoint>::toList).flatten().size)
}


private fun PathPoint.findTrails(map: List<List<PathPoint>>, list: List<PathPoint>): List<List<PathPoint>> {
    if (this.height == 9) {
        return listOf(list + this)
    } else {
        val left = if (j - 1 >= 0 && map[this.i][this.j - 1].height == this.height + 1) {
            map[this.i][this.j - 1].findTrails(map, list + this)
        } else {
            listOf()
        }
        val right = if (j + 1 < map[0].size && map[this.i][this.j + 1].height == this.height + 1) {
            map[this.i][this.j + 1].findTrails(map, list + this)
        } else {
            listOf()
        }
        val up = if (i - 1 >= 0 && map[this.i - 1][this.j].height == this.height + 1) {
            map[this.i - 1][this.j].findTrails(map, list + this)
        } else {
            listOf()
        }
        val down = if (i + 1 < map.size && map[this.i + 1][this.j].height == this.height + 1) {
            map[this.i + 1][this.j].findTrails(map, list + this)
        } else {
            listOf()
        }
        return left + right + up + down
    }
}

fun secondStar(input: String) {
    val map = input.lines().mapIndexed { i, l -> l.mapIndexed { j, c -> PathPoint(i, j, c.digitToInt()) } }
    val groundPoints = map.flatten().filter { n -> n.height == 0 }
    val trails = groundPoints.map { p -> p.findTrails(map, listOf()) }
    val trailheads = trails.map { tl ->
        tl.size
    }.reduce(Integer::sum)

    println(trailheads)
    println(trails.size)
}

data class PathPoint(val i: Int, val j: Int, val height: Int)
