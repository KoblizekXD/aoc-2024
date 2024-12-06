package lol.koblizek.aoc

import lol.koblizek.aoc.util.readInput

val types = listOf('<', '>', '^', 'v')

fun findPos(map: List<CharArray>): Pair<Int, Int> {
    map.forEachIndexed { y, arr ->
        arr.find { it in types }?.let { return Pair(y, arr.indexOf(it)) }
    }
    return Pair(-1, -1)
}

fun move(map: List<CharArray>, pos: Pair<Int, Int>): List<CharArray> {
    var (y, x) = pos
    var type = map[y][x]
    val newMap = map.toMutableList()
    fun tryRotate(): Boolean {
        type = when {
            type == '<' && newMap[y - 1][x] != '#' -> '^'
            type == '^' && newMap[y][x + 1] != '#' -> '>'
            type == '>' && newMap[y + 1][x] != '#' -> 'v'
            type == 'v' && newMap[y][x - 1] != '#' -> '<'
            else -> return false
        }
        return true
    }
    fun tryMoveInDirection() {
        when (type) {
            '<' -> {
                if (x != 0 && newMap[y][x - 1] != '#') {
                    newMap[y][x - 1] = '<'
                    newMap[y][x] = 'X'
                    x--
                } else if (x == 0 || !tryRotate()) return
            }
            '>' -> {
                if (x + 1 != newMap[0].size && newMap[y][x + 1] != '#') {
                    newMap[y][x + 1] = '>'
                    newMap[y][x] = 'X'
                    x++
                } else if (x + 1 == newMap[0].size || !tryRotate()) return
            }
            '^' -> {
                if (y != 0 && newMap[y - 1][x] != '#') {
                    newMap[y - 1][x] = '^'
                    newMap[y][x] = 'X'
                    y--
                } else if (y == 0 || !tryRotate()) return
            }
            'v' -> {
                if (y + 1 != map.size && newMap[y + 1][x] != '#') {
                    newMap[y + 1][x] = 'v'
                    newMap[y][x] = 'X'
                    y++
                } else if (y + 1 == map.size || !tryRotate()) return
            }
        }
        tryMoveInDirection()
    }
    tryMoveInDirection()
    return newMap
}

fun main() {
    val input = readInput(6, false).map { it.toCharArray() }
    var sum = 0
    move(input, findPos(input)).forEach {
        println(it)
        sum += it.count { types.contains(it) || it == 'X' }
    }
    println(sum)
}