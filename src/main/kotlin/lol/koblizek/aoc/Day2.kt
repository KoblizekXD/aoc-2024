package lol.koblizek.aoc

import lol.koblizek.aoc.util.readInput
import kotlin.math.abs

fun main() {
    val lines = readInput(2, false).map { it.split(" ").map { it.toInt() } }
    
    var safe = 0;
    lines.forEach { l ->
        val isIncreasing = l[1] - l[0] > 0
        var canBeSafe = true
        var freeLevel = false
        for (i in 0..(l.size - 2)) {
            val diff = l[i + 1] - l[i]
            if (diff > 0 != isIncreasing
                || abs(diff) == 0 || abs(diff) > 3) {
                if (!freeLevel) {
                    freeLevel = true
                } else {
                    canBeSafe = false
                    break
                }
            }
        }
        if (canBeSafe) {
            safe++
        }
    }
    println(safe)
}