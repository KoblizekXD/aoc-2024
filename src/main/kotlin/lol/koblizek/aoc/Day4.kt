package lol.koblizek.aoc

import lol.koblizek.aoc.util.readInput

fun main() {
    val input = readInput(4, false).map { it.toCharArray() }
    
    var sum = 0
    var p2 = 0
    var x = 0
    val types = arrayOf("XMAS", "SAMX")
    val types2 = arrayOf("MAS", "SAM")
    input.forEachIndexed { i, line ->
        line.forEachIndexed { j, char ->
            if (i - 1 >= 0 
                && i + 1 < input.size
                && j + 1 < line.size
                && j - 1 >= 0
                && char == 'A' 
                && types2.contains(line.slice(j - 1..j + 1)
                    .mapIndexed { k, _ -> input[i - 1 + k][j - 1 + k] }.joinToString(""))
                && types2.contains(line.slice(j - 1..j + 1)
                    .mapIndexed { k, _ -> input[i + 1 - k][j - 1 + k] }.joinToString(""))) {
                p2++
            }
            
            if (j + 3 < line.size) {
                if (types.contains(line.slice(j..j+3).joinToString(""))) {
                    sum++
                }
                if (i + 3 < input.size 
                    && types.contains(line.slice(j..j+3)
                        .mapIndexed { k, _ -> input[i + k][j + k] }.joinToString(""))) {
                    sum++
                    x++
                }
                if (i - 3 >= 0
                    && types.contains(line.slice(j..j+3)
                        .mapIndexed { k, _ -> input[i - k][j + k] }.joinToString(""))) {
                    sum++ 
                }
            }
            if (i + 3 < input.size
                && types.contains("abcd".mapIndexed { k, _ -> input[i + k][j] }.joinToString(""))) {
                sum++
            }
        }
    }
    println(sum)
    println(p2)
}