package lol.koblizek.aoc.util

fun readInput(day: Int, example: Boolean = false): List<String> {
    val res = if (example) "day$day-example.txt" else "day$day.txt"
    return {}.javaClass.getResource("/input/$res")!!.readText().lines()
}

fun <V> getCombinations(map: List<V>): List<Pair<V, V>> {
    val res = mutableListOf<Pair<V, V>>()
    for (i in map.indices) {
        for (j in i + 1 until map.size) {
            res.add(map[i] to map[j])
        }
    }
    return res
}

fun Any.println() = println(this)
fun Any.print() = print(this)

data class Location(val x: Int, val y: Int) {
    operator fun plus(other: Location) = Location(x + other.x, y + other.y)
    operator fun minus(other: Location) = Location(x - other.x, y - other.y)
    operator fun times(other: Int) = Location(x * other, y * other)
    operator fun div(other: Int) = Location(x / other, y / other)
}

fun Collection<String>.printStrings() {
    forEach { println(it) }
}

fun Collection<CharArray>.printArrays() {
    forEach { println(it.joinToString("")) }
}

fun Collection<Collection<Char>>.printLists() {
    forEach { println(it.joinToString("")) }
}

fun calculateOppositePoint(midpoint: Pair<Int, Int>, pointB: Pair<Int, Int>): Pair<Int, Int> {
    val cx = 2 * midpoint.first - pointB.first
    val cy = 2 * midpoint.second - pointB.second
    return Pair(cx, cy)
}