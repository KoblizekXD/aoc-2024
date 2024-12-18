package lol.koblizek.aoc

import lol.koblizek.aoc.util.print
import lol.koblizek.aoc.util.println
import lol.koblizek.aoc.util.readInput
import kotlin.math.pow
import kotlin.math.truncate

fun main() {
    val list = listOf<Int>(2,4,1,3,7,5,0,3,4,3,1,5,5,5,3,0)
    var A = 22571680L
    A.toString(2).println()
    var B = 0L
    var C = 0L
    val toCheck = mutableListOf<Int>()
    while (A != 0L) {
        B = A % 8
        B = (B xor 3)
        C = A.shr(B.toInt())
        A = A.shr(3)
        B = (B xor C.toLong())
        B = (B xor 5)
        toCheck.add((B % 8).toInt())
    }
    toCheck.println()
    toCheck.map { it.toString(2) }.joinToString("")
        .println()
}

fun reconstructA(list: List<Int>): Long {
    var A = 0L
    for (i in list.reversed()) {
        
    }
    return A
}

fun try3Bits() {
    for (i in 0..7) {
        var A = (2.0.pow(3) + i).toLong()
        var B = A % 8L
        var C = 0L
        B = (B xor 3)
        C = A.shr(B.toInt())
        A = A.shr(3)
        B = (B xor C.toLong())
        B = (B xor 5)
    }
}

fun d() {
    val input = readInput(17, false)
    var a = input[0].substring(12).toInt()
    var b = input[1].substring(12).toInt()
    var c = input[2].substring(12).toInt()
    val instructionData = input[4].substring(9).split(",")
        .map { it.toInt() }
    var isp = 0
    val toPrint = mutableListOf<Int>()
    
    while (isp < instructionData.size) {
        var insn = instructionData[isp]
        when (insn) {
            0 -> { // adv
                val target = when (instructionData[isp + 1]) {
                    4 -> a
                    5 -> b
                    6 -> c
                    7 -> throw Exception("Invalid target")
                    else -> instructionData[isp + 1]
                }
                a = truncate(a / 2.0.pow(target.toDouble())).toInt()
                println("a = a / 2^$target")
            }
            1 -> { // bxl
                println("b = b xor ${instructionData[isp + 1]}")
                b = b.xor(instructionData[isp + 1])
            }
            2 -> { // bst
                val target = when (instructionData[isp + 1]) {
                    4 -> a
                    5 -> b
                    6 -> c
                    7 -> throw Exception("Invalid target")
                    else -> instructionData[isp + 1]
                }
                println("b = $target >> 3")
                b = target % 8 
            }
            3 -> { // jnz
                if (a != 0) {
                    println("if (a != 0) jmp ${instructionData[isp + 1]}")
                    isp = instructionData[isp + 1]
                    continue
                }
            }
            4 -> { // bxc
                println("b = b xor c")
                b = b.xor(c)
            }
            5 -> { // out
                val target = when (instructionData[isp + 1]) {
                    4 -> a
                    5 -> b
                    6 -> c
                    7 -> throw Exception("Invalid target")
                    else -> instructionData[isp + 1]
                } % 8
                println("print $target")
                toPrint.add(target)
            }
            6 -> { // bdv
                val target = when (instructionData[isp + 1]) {
                    4 -> a
                    5 -> b
                    6 -> c
                    7 -> throw Exception("Invalid target")
                    else -> instructionData[isp + 1]
                }
                println("b = a / 2^$target")
                b = truncate(a / 2.0.pow(target.toDouble())).toInt()
            }
            7 -> { // cdv
                val target = when (instructionData[isp + 1]) {
                    4 -> a
                    5 -> b
                    6 -> c
                    7 -> throw Exception("Invalid target")
                    else -> instructionData[isp + 1]
                }
                println("c = a / 2^$target")
                c = truncate(a / 2.0.pow(target.toDouble())).toInt()
            }
        }
        isp += 2
    }
    a.println()
    b.println()
    println(toPrint.joinToString(","))
}