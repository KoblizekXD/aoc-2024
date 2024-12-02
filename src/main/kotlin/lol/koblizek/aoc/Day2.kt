package lol.koblizek.aoc

import lol.koblizek.aoc.util.readInput
import sun.tools.jconsole.VariableGridLayout
import kotlin.math.abs

fun checkIfSafe(l: List<Int>): Boolean {
    val isIncreasing = l[1] - l[0] > 0
    var canBeSafe = true
    
    l.forEachIndexed { i, v ->
        if (i == l.size - 1) return@forEachIndexed
        
        val diff = l[i + 1] - v
        val absDiff = abs(diff)
        
        if (isIncreasing && diff < 0 || !isIncreasing && diff > 0
            || absDiff < 1 || absDiff > 3) {
            canBeSafe = false
            return@forEachIndexed
        }
    }
    
    return canBeSafe
}

fun main() {
    val lines = readInput(2, false).map { it.split(" ").map { it.toInt() } }
    
    var safe = 0;
    lines.forEachIndexed { i, l ->
        val x = checkIfSafe(l)
        if (x) safe++
        else {
            run {
                l.forEachIndexed { i, v ->
                    val list = l.toMutableList()
                    list.removeAt(i)
                    if (checkIfSafe(list)) {
                        safe++
                        return@run
                    }
                }
            }
        }
    }
    println(safe)
}