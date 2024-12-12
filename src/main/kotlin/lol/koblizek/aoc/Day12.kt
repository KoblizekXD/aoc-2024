package lol.koblizek.aoc

import lol.koblizek.aoc.util.Grid
import lol.koblizek.aoc.util.distance

fun scanForMe(grid: Grid, point: Grid.Point): List<Grid.Point> {
    val visited = mutableSetOf<Grid.Point>()
    fun scan2(grid: Grid, point: Grid.Point): List<Grid.Point> {
        if (point in visited) return emptyList() // Skip already visited points

        visited.add(point) // Mark the current point as visited
        val island = mutableListOf(point) // Start a new island group

        grid.getNeighbours(point).forEach {
            if (it !in visited && grid[it] == grid[point]) {
                island.addAll(scan2(grid, it)) // Recur for neighbors of the same character
            }
        }

        return island
    }
    return scan2(grid, point)
}

fun <K, V> Map<K, List<V>>.valueContains(value: V): Boolean {
    return this.values.any { it.contains(value) }
}

fun <V> List<List<V>>.valueContains(value: V): Boolean {
    return this.any { it.contains(value) }
}

// Suppose all of them are connected...
fun findPerimeter(points: List<Grid.Point>): Int {
    var sum = 0
    points.forEach { point ->
        val neighbours = points.filter { point.distance(it) == 1 }
        sum += 4 - neighbours.size
    }
    return sum
}

fun getSideCount(points: List<Grid.Point>): Int {
    val exposed = points.filter { points.count { p ->
        it.validNeighbours3x3().contains(p)
    } < 9 }.distinct()
    val points = exposed.map { 9 - points.count { p -> it.validNeighbours3x3().contains(p) } - 1 }.sum()

    return points
    // return points
}

fun findPrice(points: List<Grid.Point>): Int {
    return findPerimeter(points) * points.size
}

fun main() {
    val grid = Grid(12, true)
    val visited = mutableSetOf<Grid.Point>()
    var sum = 0
    grid.forEach {
        val isl = scanForMe(grid, it)
        if (isl.any { visited.contains(it) }) return@forEach
        visited.addAll(isl)
        val price = findPrice(isl)
        getSideCount(isl)
        println("Island ${grid[isl[0]]} has price $price")
        sum += price
    }
    println("Total price: $sum")
}