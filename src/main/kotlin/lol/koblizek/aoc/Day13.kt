package lol.koblizek.aoc

import lol.koblizek.aoc.util.println
import lol.koblizek.aoc.util.readInput

data class Input(val aOffsetBy: Pair<Long, Long>, val bOffsetBy: Pair<Long, Long>, val target: Pair<Long, Long>) {
    /**
     * Returns the numbers of button A and button B needed to click to reach the certain target.
     */
    fun compute(): Pair<Long, Long> {
        var b = (-((aOffsetBy.first * (target.second.toDouble() / aOffsetBy.second) - target.first) / (-aOffsetBy.first.toDouble() * (bOffsetBy.second.toDouble() / aOffsetBy.second) + bOffsetBy.first)))
        var a = ((target.second - b * bOffsetBy.second.toDouble()) / aOffsetBy.second)
        return a.toLong() to b.toLong()
    }
    
    fun getPrice(): Long? {
        val (a, b) = compute()
        if (a * aOffsetBy.second + b * bOffsetBy.second != target.second && a * aOffsetBy.first + b * bOffsetBy.first != target.first) {
            return null
        }
        return getTokenPrice(a to b).toLong()
    }
}

fun main() {
    val input = readInput(13, true).filter { it.isNotEmpty() }.chunked(3) {
        val line1 = it[0].substring(10).split(", ")
        val line2 = it[1].substring(10).split(", ")
        val target = it[2].substring(7).split(", ")
        Input(
            Pair<Long, Long>(line1[0].substring(2).toLong(), line1[1].substring(2).toLong()),
            Pair<Long, Long>(line2[0].substring(2).toLong(), line2[1].substring(2).toLong()),
            Pair<Long, Long>(target[0].substring(2).toLong(), target[1].substring(2).toLong()),
        )
    }
    
    val data = input.mapNotNull { it.getPrice() }
    data.sum().println()
}

fun getTokenPrice(pair: Pair<Long, Long>): Long {
    return pair.first * 3L + pair.second * 1L
}