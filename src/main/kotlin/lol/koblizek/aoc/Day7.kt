package lol.koblizek.aoc

import lol.koblizek.aoc.util.readInput
import kotlin.math.pow

data class InputLine(val total: Long, val nums: List<Long>)

enum class Operation {
    ADD, MULTIPLY, CONCAT
}

fun main() {
    val input = readInput(7, false).map { 
        var split = it.split(": ")
        InputLine(split[0].toLong(), split[1].split(" ").map { it.toLong() })
    }
    
    var total: Long = 0
    input.forEach {
        if (it.total == it.nums.sum()) {
            total += it.total
        } else {
            val opSlots = it.nums.size - 1
            var found = false
            for (combination in 0 until (3.toDouble().pow(opSlots).toInt())) {
                val operations = mutableListOf<Operation>()
                var temp = combination
                for (i in 0 until opSlots) {
                    operations.add(Operation.entries[temp % 3])
                    temp /= 3
                }
                operations.reverse()
                it.nums.reduceIndexed { index, acc, num ->
                    when (operations[index - 1]) {
                        Operation.ADD -> acc + num
                        Operation.MULTIPLY -> acc * num
                        Operation.CONCAT -> (acc.toString() + num.toString()).toLong()
                    }
                }.let { data ->
                    if (data == it.total) {
                        total += data
                        found = true
                    }
                }
                if (found) break
            }
        }
    }
    println(total)
}