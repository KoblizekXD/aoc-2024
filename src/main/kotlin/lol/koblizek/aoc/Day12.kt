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

/*
......
.XXX..
.XXX..
..XX..
......

.......
..XXX..
.XXXXX.
..X.X..
.......
*/
fun getSideCount(grid: Grid, points: List<Grid.Point>): Int {
    val exposed = points.filter { points.count { p ->
        it.validNeighbours().filter { !grid.isOutOfBounds(it) }.contains(p)
    } < 4 }
    var innerVertices = 0
    
    points.forEach { point ->
        /*
        .XXX.
        .X.X.
        .XXX.
        */
        // Top left
        if (point + Grid.Point(1, 0) in points && point + Grid.Point(0, 1) in points
            && point + Grid.Point(1, 1) !in points) innerVertices++
        // Top right
        if (point + Grid.Point(-1, 0) in points && point + Grid.Point(0, 1) in points
            && point + Grid.Point(-1, 1) !in points) innerVertices++
        // Bottom left
        if (point + Grid.Point(1, 0) in points && point + Grid.Point(0, -1) in points
            && point + Grid.Point(1, -1) !in points) innerVertices++
        // Bottom right
        if (point + Grid.Point(-1, 0) in points && point + Grid.Point(0, -1) in points
            && point + Grid.Point(-1, -1) !in points) innerVertices++
    }
    
    fun getFreeSides(point: Grid.Point): List<Grid.Point> {
        return point.neighboursClean().filter { point + it !in points }
    }
    
    var outerVertices = 0
    exposed.forEach { point ->
        // Top left
        val sides = getFreeSides(point)
        if (Grid.Point(-1, 0) in sides && Grid.Point(0, -1) in sides
            && point + Grid.Point(-1, -1) !in points)
            outerVertices++
        // Top right
        if (Grid.Point(1, 0) in sides && Grid.Point(0, -1) in sides
            && point + Grid.Point(1, -1) !in points)
            outerVertices++
        // Bottom left
        if (Grid.Point(-1, 0) in sides && Grid.Point(0, 1) in sides
            && point + Grid.Point(-1, 1) !in points)
            outerVertices++
        // Bottom right
        if (Grid.Point(1, 0) in sides && Grid.Point(0, 1) in sides
            && point + Grid.Point(1, 1) !in points)
            outerVertices++
    }
    
    return innerVertices + outerVertices
}

fun findPrice(points: List<Grid.Point>): Int {
    return findPerimeter(points) * points.size
}

fun findPrice2(points: List<Grid.Point>, sideCount: Int): Int {
    return sideCount * points.size
}

fun main() {
    val grid = Grid(12, true)
    val visited = mutableSetOf<Grid.Point>()
    var sum = 0
    grid.forEach {
        val isl = scanForMe(grid, it)
        if (isl.any { visited.contains(it) }) return@forEach
        visited.addAll(isl)
        val sides = getSideCount(grid, isl)
        val price = findPrice(isl)
        val price2 = findPrice2(isl, sides)
        // println("Island ${grid[isl[0]]} has price $price")
        println("Island ${grid[isl[0]]} has price $price2")
        sum += price2
    }
    println("Total price: $sum")
}