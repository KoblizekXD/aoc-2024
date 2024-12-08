package lol.koblizek.aoc

import lol.koblizek.aoc.util.calculateOppositePoint
import lol.koblizek.aoc.util.getCombinations
import lol.koblizek.aoc.util.printLists
import lol.koblizek.aoc.util.println
import lol.koblizek.aoc.util.readInput

data class Point(val x: Int, val y: Int, val char: Char)

fun main() {
    val input = readInput(8, false).toMutableList().map { it.toMutableList() }
    
    val pointCombinations = input.flatMapIndexed { i, arr -> 
        arr.mapIndexedNotNull { j, c -> 
            if (c.isLetterOrDigit()) Point(j, i, c) else null
        }
    }.groupBy { it.char }.map { getCombinations(it.value) }
    val points = mutableMapOf<Pair<Int, Int>, Int>()

    pointCombinations.forEach { 
        it.forEach { (p1, p2) ->
            var midPoint = p1.x to p1.y
            var tmpP = calculateOppositePoint(midPoint, p2.x to p2.y)
            while (tmpP.first >= 0 && tmpP.second >= 0 && tmpP.first < input[0].size && tmpP.second < input.size) {
                input[tmpP.second][tmpP.first] = '#'
                points.compute(tmpP) { _, v -> (v ?: 0) + 1 }
                val temp = tmpP
                tmpP = calculateOppositePoint(tmpP, midPoint)
                midPoint = temp
            }
            midPoint = p2.x to p2.y
            tmpP = calculateOppositePoint(midPoint, p2.x to p2.y)
            calculateOppositePoint(midPoint, p1.x to p1.y).let { tmpP = it }
            while (tmpP.first < input[0].size && tmpP.second < input.size && tmpP.first >= 0 && tmpP.second >= 0) {
                input[tmpP.second][tmpP.first] = '#'
                points.compute(tmpP) { _, v -> (v ?: 0) + 1 }
                val temp = tmpP
                tmpP = calculateOppositePoint(tmpP, midPoint)
                midPoint = temp
            }
        }
    }
    input.printLists()
    var counter = 0
    input.forEach { 
        it.forEach { 
            if (it != '.')
                counter++
        }
    }
    counter.println()
}