package lol.koblizek.aoc

import lol.koblizek.aoc.util.readInput

val types = listOf('<', '>', '^', 'v')

enum class Direction(val char: Char) {
    WEST('<'), EAST('>'), NORTH('^'), SOUTH('v');
    
    companion object {
        fun fromChar(char: Char): Direction? {
            return entries.find { it.char == char }
        }
    }
    
    fun turnRight(): Direction {
        return when (this) {
            WEST -> NORTH
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
        }
    }
    fun turnLeft(): Direction {
        return when (this) {
            WEST -> SOUTH
            SOUTH -> EAST
            EAST -> NORTH
            NORTH -> WEST
        }
    }
    
    // Returns the values to offset the current position by to move in the current direction
    fun nextPos(): Pair<Int, Int> {
        return when (this) {
            WEST -> Pair(0, -1)
            EAST -> Pair(0, 1)
            NORTH -> Pair(-1, 0)
            SOUTH -> Pair(1, 0)
        }
    }
    fun prevPos(): Pair<Int, Int> {
        return when (this) {
            WEST -> Pair(0, 1)
            EAST -> Pair(0, -1)
            NORTH -> Pair(1, 0)
            SOUTH -> Pair(-1, 0)
        }
    }
    fun nextPos(y: Int, x: Int): Pair<Int, Int> {
        val (dy, dx) = nextPos()
        return Pair(y + dy, x + dx)
    }
    fun prevPos(y: Int, x: Int): Pair<Int, Int> {
        val (dy, dx) = prevPos()
        return Pair(y + dy, x + dx)
    }
}

fun findPos(map: List<CharArray>): Pair<Int, Int> {
    map.forEachIndexed { y, arr ->
        arr.find { it in types }?.let { return Pair(y, arr.indexOf(it)) }
    }
    return Pair(-1, -1)
}

fun main() {
    val input = readInput(6, false).map { it.toCharArray() }.toMutableList()
    var sum = 0
    
    input.forEachIndexed { i, arr -> 
        arr.forEachIndexed { j, c -> 
            val new = input.toMutableList().map { it.copyOf() }
            var (y, x) = findPos(input)
            var direction = Direction.fromChar(input[y][x])!!
            val positions = mutableSetOf<Pair<Pair<Int, Int>, Direction>>()
            var looping = false
            if (c == '.') new[i][j] = '#'
            while (true) {
                new[y][x] = 'X'
                val (dy, dx) = direction.nextPos()
                y += dy
                x += dx
                if (y !in new.indices || x !in new[y].indices)
                    break
                if (new[y][x] == '#') {
                    direction.prevPos().let { (dy, dx) -> y += dy; x += dx }
                    direction = direction.turnRight()
                    if (Pair(y, x) to direction in positions) {
                        looping = true
                        break
                    }
                    positions.add(Pair(y, x) to direction)
                }
                new[y][x] = direction.char
            }
            if (looping) sum++
        }
    }
    println(sum)
}