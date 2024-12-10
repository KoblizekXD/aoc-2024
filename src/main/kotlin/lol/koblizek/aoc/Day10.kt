package lol.koblizek.aoc

import lol.koblizek.aoc.util.Grid

val scores: HashMap<Grid.Point, MutableSet<Grid.Point>> = hashMapOf()

fun lookUp(point: Grid.Point, head: Grid.Point, grid: Grid, targetNumber: Int) {
    val chars = grid.getNeighbouringChars(point) { it == targetNumber.digitToChar() }
    if (chars.isEmpty()) {
        return
    } else {
        chars.forEach {
            if (targetNumber == 9) {
                scores[head]!!.add(it)
            } else lookUp(it, head, grid, targetNumber + 1)
        }
    }
}

fun d10p1() {
    val grid = Grid(10, false)
    val zeros = grid.getAll { it == '0' }

    zeros.forEach {
        scores[it] = mutableSetOf()
        lookUp(it, it, grid, 1)
    }
    println(scores.map { it.value.size }.sum())
}

val p2scores: MutableMap<Grid.Point, Int> = mutableMapOf() 

fun lookUp2(point: Grid.Point, head: Grid.Point, grid: Grid, targetNumber: Int) {
    val chars = grid.getNeighbouringChars(point) { it == targetNumber.digitToChar() }
    if (chars.isEmpty()) {
        return
    } else {
        chars.forEach {
            if (targetNumber == 9) {
                p2scores[head] = p2scores[head]!! + 1
            } else lookUp2(it, head, grid, targetNumber + 1)
        }
    }
}

fun d10p2() {
    val grid = Grid(10, false)
    val zeros = grid.getAll { it == '0' }

    zeros.forEach {
        p2scores[it] = 0
        lookUp2(it, it, grid, 1)
    }
    println(p2scores.map { it.value }.sum())
}

fun main() {
    d10p2()
}