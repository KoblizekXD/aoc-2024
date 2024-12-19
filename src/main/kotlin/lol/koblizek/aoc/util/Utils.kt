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

fun List<String>.splitAtEmpty(): Pair<List<String>, List<String>> {
    val index = indexOf("")
    return Pair(subList(0, index), subList(index + 1, size))
}

fun splitNumber(number: Long, splitBy: Long): List<Pair<Long, Long>> {
    val list = mutableListOf<Pair<Long, Long>>()
    var temp = 0L
    var by = number / splitBy
    for (i in 0 until splitBy) {
        list.add(Pair(temp, temp + by - 1))
        temp += by
    }
    return list
}

fun <K, V> MutableMap<K, MutableList<V>>.putOrUpdate(key: K, value: V) {
    if (containsKey(key)) {
        get(key)!!.add(value)
    } else {
        put(key, mutableListOf(value))
    }
}

fun <T> Collection<List<T>>.transpose(): List<List<T>> {
    return (0 until first().size).map { col -> map { it[col] } }
}