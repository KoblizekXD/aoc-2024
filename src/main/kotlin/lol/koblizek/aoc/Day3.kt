package lol.koblizek.aoc

import lol.koblizek.aoc.util.readInput
import kotlin.text.Regex

fun main() {
    val input = readInput(3, false)

    val regex = Regex("mul\\(?(\\d+),?(\\d+)\\)|do\\(\\)|don't\\(\\)")
    
    val results = regex.findAll(input.joinToString())
    
    var enabled = true
    var sum = 1
    results.forEach {
        if (it.value.startsWith("don't")) {
            enabled = false
        } else if (it.value.startsWith("do")) {
            enabled = true
        } else {
            if (enabled) {
                sum += (it.groupValues[1].toInt() * it.groupValues[2].toInt())
            }
        }
    }
    println(sum - 1)
}