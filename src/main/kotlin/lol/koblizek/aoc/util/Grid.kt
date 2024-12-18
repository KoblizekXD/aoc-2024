package lol.koblizek.aoc.util

import lol.koblizek.aoc.util.Grid.Point
import java.io.File
import kotlin.math.abs

data class Grid(val input: MutableList<CharArray>) {
    
    constructor(day: Int, example: Boolean = false) : this(readInput(day, example).map { it.toCharArray() }.toMutableList())
    constructor(width: Int, height: Int) : this(MutableList(height) { 
        CharArray(width) { '.' }
    })
    
    fun print() {
        input.forEach { println(it.joinToString("")) }
    }
    
    fun appendTo(file: String) {
        File(file).appendText(input.map { it.joinToString("") }.joinToString("\n") + "\n\n\n")
    }
    
    val width: Int
        get() = input[0].size
    val height: Int
        get() = input.size
    
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
    
    operator fun set(point: Point, value: Char) {
        set(point.x, point.y, value)
    }

    fun getNeighbours(point: Point): List<Point> {
        return listOf(
            Point(0, -1),
            Point(-1, 0), Point(1, 0),
            Point(0, 1)
        ).map { point + it }.filter { it.x in 0 until input[0].size && it.y in 0 until input.size }
    }
    
    fun forEach(action: (Point) -> Unit) {
        input.forEachIndexed { y, arr ->
            arr.forEachIndexed { x, _ ->
                action(x pairedWith y)
            }
        }
    }
    
    fun getNeighbouringChars(point: Point): List<Char> {
        return getNeighbours(point).map { getPoint(it) }
    }
    
    fun isOutOfBounds(point: Point): Boolean {
        return point.x !in 0 until input[0].size || point.y !in 0 until input.size
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
    
    operator fun get(point: Point) = getPoint(point)
    
    fun mirror(midpoint: Point, point: Point): Point {
        return Point(2 * midpoint.x - point.x, 2 * midpoint.y - point.y)
    }
    
    data class Point(val x: Int, val y: Int): Comparable<Point> {
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
        
        fun validNeighbours(): List<Point> {
            return listOf(
                this + Point(0, -1),
                this + Point(-1, 0), this + Point(1, 0),
                this + Point(0, 1)
            )
        }
        
        fun neighboursClean(): List<Point> {
            return listOf(
                Point(0, -1),
                Point(-1, 0), Point(1, 0),
                Point(0, 1)
            )
        }
        
        fun validNeighbours3x3(): List<Point> {
            return listOf(
                this + Point(-1, -1), this + Point(0, -1), this + Point(1, -1),
                this + Point(-1, 0), this, this + Point(1, 0),
                this + Point(-1, 1), this + Point(0, 1), this + Point(1, 1)
            )
        }

        override fun compareTo(other: Point): Int {
            return compareValuesBy(this, other, Point::x, Point::y)
        }
    }
    
    fun copy(): Grid {
        return Grid(input.map { it.copyOf() }.toMutableList())
    }
    
    fun find(predicate: (Char) -> Boolean): Point? {
        input.forEachIndexed { y, arr ->
            arr.forEachIndexed { x, c ->
                if (predicate(c))
                    return Point(x, y)
            }
        }
        return null
    }
    
    fun findAll(predicate: (Char) -> Boolean): List<Point> {
        val res = mutableListOf<Point>()
        input.forEachIndexed { y, arr ->
            arr.forEachIndexed { x, c ->
                if (predicate(c))
                    res.add(Point(x, y))
            }
        }
        return res
    }
}

infix fun Grid.Point.distanceTo(other: Grid.Point): Grid.Point {
    return abs(x - other.x) pairedWith abs(y - other.y)
}

infix fun Grid.Point.faces(other: List<Grid.Point>): Boolean {
    return other.any { it.distance(this) == 1 }
} 

infix fun Grid.Point.distance(other: Grid.Point): Int {
    return abs(x - other.x) + abs(y - other.y)
}

infix fun Int.pairedWith(other: Int): Grid.Point {
    return Grid.Point(this, other)
}

enum class Direction(val angle: Int) {
    NORTH(0), EAST(90), SOUTH(180), WEST(270);
    
    operator fun plus(other: Direction) = Direction.of((angle + other.angle) % 360)
    operator fun minus(other: Direction) = Direction.of((angle - other.angle) % 360)
    
    fun toPoint(): Point {
        return when (this) {
            NORTH -> Point(0, -1)
            EAST -> Point(1, 0)
            SOUTH -> Point(0, 1)
            WEST -> Point(-1, 0)
        }
    }

    operator fun component1() = this.toPoint().x
    operator fun component2() = this.toPoint().y

    companion object {
        fun of(angle: Int): Direction {
            return when ((angle + 720) % 360) {
                0 -> NORTH
                90 -> EAST
                180 -> SOUTH
                270 -> WEST
                else -> throw IllegalArgumentException("Invalid rotation: $angle")
            }
        }
    }
}