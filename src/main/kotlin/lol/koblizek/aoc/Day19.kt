package lol.koblizek.aoc

import lol.koblizek.aoc.util.readInput

fun main() {
    val read = readInput(19, false)
    val chars = read[0].split(", ")
    var regex = "(${chars.sortedBy { it.length }.joinToString("|")})*".toRegex()
    var counter = 0
    read.subList(2, read.size).forEach {
        val result = regex.matchEntire(it)
        if (result != null) {
            counter++
        }
    }
    println(counter)
}