package lol.koblizek.aoc

import lol.koblizek.aoc.util.println
import lol.koblizek.aoc.util.readInput

val digits = "0123456789".toCharArray()

fun main() {
    val input = readInput(9, false)[0]
    println(input.length)
    var res = input.mapIndexedNotNull { i, c ->
        if (i % 2 == 0) {
            ((i / 2).toString() + " ").repeat(c.digitToInt()).trim()
        } else ".".repeat(c.digitToInt())
    }.flatMap { if (!it.contains(".")) it.split(" ") else it.toCharArray().map { it.toString() } }
        .filter { it.isNotEmpty() }
        .toMutableList()
    println(res)
    while (res.subList(0, res.indexOfLast { it.toIntOrNull() != null } + 1).any { it[0] == '.' }) {
        val replacingIndex = res.indexOfLast { it.toIntOrNull() != null }
        val number = res[replacingIndex]
        /*val indexes = res.withIndex().filter { it.value[0] == '.' }.take(number.length).map { it.index }
        indexes.forEachIndexed { i, index -> res[index] = number[i].toString() }*/
        res.indexOf(".").let { res[it] = number }
        res[replacingIndex] = "."
    }
    println(res)
    res.subList(0, res.indexOf(".")).mapIndexed { i, it ->
        i * it.toLong()
    }.sum().println()
}