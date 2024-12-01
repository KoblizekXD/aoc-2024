package lol.koblizek.aoc.util

fun readInput(day: Int, example: Boolean = false): List<String> {
    val res = if (example) "day$day-example.txt" else "day$day.txt"
    return {}.javaClass.getResource("/input/$res")!!.readText().lines()
}