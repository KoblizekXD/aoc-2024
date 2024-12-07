package lol.koblizek.aoc

import lol.koblizek.aoc.util.readInput

val types = listOf('<', '>', '^', 'v')

fun findPos(map: List<CharArray>): Pair<Int, Int> {
    map.forEachIndexed { y, arr ->
        arr.find { it in types }?.let { return Pair(y, arr.indexOf(it)) }
    }
    return Pair(-1, -1)
}

fun move(map: List<CharArray>, pos: Pair<Int, Int>, placedBlockage: Pair<Int, Int> = Pair(-1, -1)): Boolean {
    var (y, x) = pos
    var type = map[y][x]
    val mutableMap = map.toMutableList()
    fun tryRotate(newMap: MutableList<CharArray>): Boolean {
        type = when {
            type == '<' && newMap[y - 1][x] != '#' -> '^'
            type == '^' && newMap[y][x + 1] != '#' -> '>'
            type == '>' && newMap[y + 1][x] != '#' -> 'v'
            type == 'v' && newMap[y][x - 1] != '#' -> '<'
            else -> return false
        }
        return true
    }
    fun tryMoveInDirection(newMap: MutableList<CharArray>, checkLoop: Boolean = false): Boolean {
        when (type) {
            '<' -> {
                if (x != 0 && (newMap[y][x - 1] != '#' || placedBlockage.first == y && placedBlockage.second == x - 1)) {
                    if (checkLoop && placedBlockage.first == y && placedBlockage.second == x - 1) return true
                    newMap[y][x - 1] = '<'
                    newMap[y][x] = 'X'
                    x--
                } else if (x == 0 || !tryRotate(newMap)) return false
            }
            '>' -> {
                if (x + 1 != newMap[0].size && (newMap[y][x + 1] != '#' || placedBlockage.first == y && placedBlockage.second == x + 1)) {
                    if (checkLoop && newMap[y][x] == 'X' && placedBlockage.first == y && placedBlockage.second == x + 1) return true
                    newMap[y][x + 1] = '>'
                    newMap[y][x] = 'X'
                    x++
                } else if (x + 1 == newMap[0].size || !tryRotate(newMap)) return false
            }
            '^' -> {
                if (y != 0 && (newMap[y - 1][x] != '#' || placedBlockage.first == y - 1 && placedBlockage.second == x)) {
                    if (checkLoop && newMap[y][x] == 'X' && placedBlockage.first == y - 1 && placedBlockage.second == x) return true
                    newMap[y - 1][x] = '^'
                    newMap[y][x] = 'X'
                    y--
                } else if (y == 0 || !tryRotate(newMap)) return false
            }
            'v' -> {
                if (y + 1 != map.size && (newMap[y + 1][x] != '#' || placedBlockage.first == y + 1 && placedBlockage.second == x)) {
                    if (checkLoop && newMap[y][x] == 'X' && placedBlockage.first == y + 1 && placedBlockage.second == x) return true
                    newMap[y + 1][x] = 'v'
                    newMap[y][x] = 'X'
                    y++
                } else if (y + 1 == map.size || !tryRotate(newMap)) return false
            }
        }
        return tryMoveInDirection(newMap, checkLoop)
    }
    return tryMoveInDirection(mutableMap, true)
}

fun main() {
    val input = readInput(6, true).map { it.toCharArray() }
    var sum = 0
    input.forEachIndexed { i, it ->
        for (j in 0 until it.size) {
            var newMap = input.map { it.toTypedArray().toCharArray() }.toMutableList()
            if (!types.contains(newMap[i][j]) && newMap[i][j] != '#' && newMap[i][j] != 'X')
                newMap[i][j] = '#'
            try {
                if (move(newMap, findPos(input), Pair(i, j)))
                    println("$i $j")
            } catch (e: StackOverflowError) {
                sum++
            }
        }
    }
//    move(input, findPos(input)).forEach {
//        sum += it.count { types.contains(it) || it == 'X' }
//    }
    println(sum)
}