package lol.koblizek.aoc.util

import kotlin.math.abs

data class Grid(val input: MutableList<CharArray>) {
    
    constructor(day: Int, example: Boolean = false) : this(readInput(day, example).map { it.toCharArray() }.toMutableList())
    
    fun print() {
        input.forEach { println(it.joinToString("")) }
    }
    
    fun count(predicate: (Char) -> Boolean): Int {
        return input.sumOf { it.count(predicate) }
    }
    
    fun get(x: Int, y: Int): Char {
        return input[y][x]
    }
    
    fun getAll(predicate: (Char) -> Boolean): List<Point> {
        val map = mutableListOf<Point>()
        input.forEachIndexed { y, arr ->
            arr.forEachIndexed { x, c ->
                if (predicate(c))
                    map.add(x pairedWith y)
            }
        }
        return map
    }

    fun getPoint(x: Int, y: Int): Point {
        return Point(x, y)
    }

    fun getPoint(point: Point): Char {
        return get(point.x, point.y)
    }
    
    fun set(x: Int, y: Int, value: Char) {
        input[y][x] = value
    }
    
    fun set(point: Point, value: Char) {
        set(point.x, point.y, value)
    }

    fun getNeighbours(point: Point): List<Point> {
        return listOf(
            Point(0, -1),
            Point(-1, 0), Point(1, 0),
            Point(0, 1)
        ).map { point + it }.filter { it.x in 0 until input[0].size && it.y in 0 until input.size }
    }
    
    fun getNeighbouringChars(point: Point): List<Char> {
        return getNeighbours(point).map { getPoint(it) }
    }
    
    fun getNeighbouringCharsWithPoints(point: Point): List<Pair<Point, Char>> {
        return getNeighbours(point).map { it to getPoint(it) }
    }
    
    fun getNeighbouringChars(point: Point, predicate: (Char) -> Boolean): List<Point> {
        return getNeighbouringCharsWithPoints(point).filter { predicate(it.second) }
            .map { it.first }
    }
    
    fun offset(point: Point, offset: Point): Point {
        return point + offset
    }
    
    operator fun contains(point: Point): Boolean {
        return point.x !in 0 until input[0].size || point.y !in 0 until input.size
    }
    
    fun mirror(midpoint: Point, point: Point): Point {
        return Point(2 * midpoint.x - point.x, 2 * midpoint.y - point.y)
    }
    
    data class Point(val x: Int, val y: Int) {
        operator fun plus(other: Point) = Point(x + other.x, y + other.y)
        operator fun minus(other: Point) = Point(x - other.x, y - other.y)
        operator fun times(other: Int) = Point(x * other, y * other)
        operator fun div(other: Int) = Point(x / other, y / other)
        
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Point
            if (x != other.x) return false
            if (y != other.y) return false
            return true
        }

        override fun hashCode(): Int {
            var result = 1
            result = 31 * result + x
            result = 31 * result + y
            return result
        }
    }
    
    fun copy(): Grid {
        return Grid(input.map { it.copyOf() }.toMutableList())
    }
}

infix fun Grid.Point.distanceTo(other: Grid.Point): Grid.Point {
    return abs(x - other.x) pairedWith abs(y - other.y)
}

infix fun Int.pairedWith(other: Int): Grid.Point {
    return Grid.Point(this, other)
}