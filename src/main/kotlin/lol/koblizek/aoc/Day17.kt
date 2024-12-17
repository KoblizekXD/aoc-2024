package lol.koblizek.aoc

import lol.koblizek.aoc.util.readInput
import kotlin.math.pow
import kotlin.math.truncate

fun main() {
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
            }
            1 -> { // bxl
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
                b = target % 8
            }
            3 -> { // jnz
                if (a != 0) {
                    isp = instructionData[isp + 1]
                    continue
                }
            }
            4 -> { // bxc
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
                c = truncate(a / 2.0.pow(target.toDouble())).toInt()
            }
        }
        isp += 2
    }
    println(toPrint.joinToString(","))
}