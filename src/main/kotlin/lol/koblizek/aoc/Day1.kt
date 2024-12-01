package lol.koblizek.aoc

import lol.koblizek.aoc.util.readInput
import kotlin.math.abs

fun main() {
    val cols = readInput(1, false).map { it.split("   ") }
    val firstCol = cols.map { it[0].toInt() }.sorted()
    val secCol = cols.map { it[1].toInt() }.sorted()

    var total = 0
    for (i in 0..(firstCol.size - 1)) {
        total += abs(firstCol[i] - secCol[i])
    }
    println(part2(firstCol, secCol))
    println(total)
}

fun part2(col1: List<Int>, col2: List<Int>): Int {
    var sum = 0
    for (i in 0..(col1.size - 1)) {
        sum += col1[i] * col2.count { it == col1[i] }
    }
    return sum
}