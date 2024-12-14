package lol.koblizek.aoc

import lol.koblizek.aoc.util.Grid
import lol.koblizek.aoc.util.println
import lol.koblizek.aoc.util.readInput

data class Day14Input(val position: Grid.Point, val velocity: Grid.Point)

const val HEIGHT = 103
const val WIDTH = 101

fun getNewPosition(grid: Grid, input: Day14Input): Day14Input {
    var new = Grid.Point(input.position.x + input.velocity.x, input.position.y + input.velocity.y)
    if (grid.isOutOfBounds(new)) {
        var newX = new.x
        var newY = new.y
        if (grid.width - 1 < new.x) {
            newX = new.x - grid.width
        } else if (new.x < 0) {
            newX = grid.width + new.x
        }
        if (grid.height - 1 < new.y) {
            newY = new.y - grid.height
        } else if (new.y < 0) {
            newY = grid.height + new.y
        }
        new = Grid.Point(newX, newY)
    }
    return Day14Input(new, input.velocity)
}

fun hasTree(grid: Grid): Boolean {
    return grid.input.any { it.joinToString("").contains("#########") }
}

fun p2(input: List<Day14Input>): Int {
    var grid = Grid(WIDTH, HEIGHT)
    var testInput = input.toMutableList()
    testInput.forEach {
        grid.set(it.position, '1')
    }
    var i = 1
    while (!hasTree(grid)) {
        val grid2 = Grid(WIDTH, HEIGHT)
        testInput = testInput.map { getNewPosition(grid2, it) }.toMutableList()
        testInput.forEach {
            grid2.set(it.position, '#')
        }
        grid = grid2
        i++
    }
    grid.print()
    return i
}

fun main() {
    val input = readInput(14, false).map { 
        val split = it.split(" ")
        val first = split[0].substring(2).split(",")
        val second = split[1].substring(2).split(",")
        Day14Input(Grid.Point(first[0].toInt(), first[1].toInt()), 
            Grid.Point(second[0].toInt(), second[1].toInt()))
    }
    p2(input).println()
    var testInput = input
    var grid = Grid(WIDTH, HEIGHT)
    val targetSeconds = 100
    testInput.forEach { 
        grid.set(it.position, '#')
    }
    for (i in 1..targetSeconds) {
        val grid2 = Grid(WIDTH, HEIGHT)
        testInput = testInput.map { getNewPosition(grid2, it) }.toMutableList()
        testInput.forEach {
            grid2.set(it.position, '#')
        }
        grid = grid2
        // grid.print()
        // println()
    }
    
    // grid.print()
    testInput.map { getQuadrant(it.position, grid) }
        .groupBy { it }
        .filter { it.key != Quadrant.UNKNOWN }
        .map { it.value.size }
        .reduce { a, b -> a * b }
        .println()
}

enum class Quadrant {
    LT,
    RT,
    LB,
    RB,
    UNKNOWN
}

fun getQuadrant(input: Grid.Point, grid: Grid): Quadrant {
    val halfNumberY = grid.height / 2
    val halfNumberX = grid.width / 2

    return when {
        input.x < halfNumberX && input.y < halfNumberY -> Quadrant.LT
        input.x < halfNumberX && input.y > halfNumberY -> Quadrant.LB
        input.x > halfNumberX && input.y < halfNumberY -> Quadrant.RT
        input.x > halfNumberX && input.y > halfNumberY -> Quadrant.RB
        else -> Quadrant.UNKNOWN
    }
}