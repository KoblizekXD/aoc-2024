package lol.koblizek.aoc

import lol.koblizek.aoc.util.Direction
import lol.koblizek.aoc.util.Grid
import lol.koblizek.aoc.util.println
import java.util.PriorityQueue

fun findP1(grid: Grid, root: Grid.Point, target: Grid.Point): Int {
    val queue = PriorityQueue<Triple<Grid.Point, Direction, Int>>(compareBy { it.third })
    queue.add(Triple(root, Direction.EAST, 0))
    val seen = hashMapOf<Pair<Grid.Point, Direction>, Int>()
    while (queue.isNotEmpty()) {
        val (point, direction, distance) = queue.poll()
        if (point == target) {
            return distance
        }
        if (seen[point to direction] != null && seen[point to direction]!! < distance) continue
        seen[point to direction] = distance
        if (grid[point + direction.toPoint()] != '#') {
            queue.add(Triple(point + direction.toPoint(), direction, distance + 1))
        }
        queue.add(Triple(point, direction + Direction.EAST, distance + 1000))
        queue.add(Triple(point, direction + Direction.WEST, distance + 1000))
    }
    return 0
}

fun main() {
    val grid = Grid(16, false)
    findP1(grid, grid.find { it == 'S' }!!, grid.find { it == 'E' }!!)
        .println()
}