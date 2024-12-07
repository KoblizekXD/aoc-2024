package lol.koblizek.aoc

import lol.koblizek.aoc.util.readInput
import java.math.BigInteger

data class InputLine(val total: BigInteger, val nums: List<BigInteger>)

enum class Operation {
    ADD, MULTIPLY
}

fun main() {
    val input = readInput(7, false).map { 
        var split = it.split(": ")
        InputLine(split[0].toBigInteger(), split[1].split(" ").map { it.toBigInteger() })
    }
    
    var total: BigInteger = BigInteger.ZERO
    input.forEach {
        if (it.total == it.nums.reduce(BigInteger::add)) {
            total = total.add(it.total)
        } else {
            for (combination in 0 until (1 shl (it.nums.size - 1))) {
                val operations = mutableListOf<Operation>()
                var found = false
                for (i in 0 until (it.nums.size - 1)) {
                    if ((combination shr i) and 1 == 1) {
                        operations.add(Operation.ADD)
                    } else {
                        operations.add(Operation.MULTIPLY)
                    }
                }
                it.nums.reduceIndexed { i, acc, n -> 
                    when (operations[i - 1]) {
                        Operation.ADD -> acc + n
                        Operation.MULTIPLY -> acc * n
                    }
                }.let { result ->
                    if (result == it.total) {
                        total += it.total
                        found = true
                    }
                }
                if (found) {
                    break
                }
            }
        }
    }
    println(total)
}