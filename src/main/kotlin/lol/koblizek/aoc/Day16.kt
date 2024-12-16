package lol.koblizek.aoc

import lol.koblizek.aoc.util.Direction
import lol.koblizek.aoc.util.Grid
import lol.koblizek.aoc.util.print
import lol.koblizek.aoc.util.println
import java.io.Serializable
import java.util.PriorityQueue

data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
) : Serializable {
    override fun toString(): String = "($first, $second, $third, $fourth)"
}

fun findP2(grid: Grid, root: Grid.Point, target: Grid.Point): List<Quadruple<Grid.Point, Direction, Int, MutableList<Grid.Point>>?> {
    val queue = PriorityQueue<Quadruple<Grid.Point, Direction, Int, MutableList<Grid.Point>>>(compareBy { it.third })
    queue.add(Quadruple(root, Direction.EAST, 0, mutableListOf()))
    val seen = hashMapOf<Pair<Grid.Point, Direction>, Int>()
    val idkMap = mutableListOf<Quadruple<Grid.Point, Direction, Int, MutableList<Grid.Point>>?>()
    while (queue.isNotEmpty()) {
        val res = queue.poll()
        val (point, direction, distance, previous) = res
        if (point == target) {
            idkMap.add(res)
            continue
        }
        if (seen[point to direction] != null && seen[point to direction]!! < distance) continue
        seen[point to direction] = distance
        if (grid[point + direction.toPoint()] != '#') {
            val new = previous.toMutableList()
            new.add(point + direction.toPoint())
            queue.add(Quadruple(point + direction.toPoint(), direction, distance + 1, new))
        }
        queue.add(Quadruple(point, direction + Direction.EAST, distance + 1000, previous))
        queue.add(Quadruple(point, direction + Direction.WEST, distance + 1000, previous))
    }
    return idkMap
}

fun findP1(grid: Grid, root: Grid.Point, target: Grid.Point): Any {
    val queue = PriorityQueue<Quadruple<Grid.Point, Direction, Int, MutableList<Grid.Point>>>(compareBy { it.third })
    queue.add(Quadruple(root, Direction.EAST, 0, mutableListOf()))
    val seen = hashMapOf<Pair<Grid.Point, Direction>, Int>()
    while (queue.isNotEmpty()) {
        val (point, direction, distance, previous) = queue.poll()
        if (point == target) {
            return distance
        }
        if (seen[point to direction] != null && seen[point to direction]!! < distance) continue
        seen[point to direction] = distance
        if (grid[point + direction.toPoint()] != '#') {
            val new = previous.toMutableList()
            new.add(point + direction.toPoint())
            queue.add(Quadruple(point + direction.toPoint(), direction, distance + 1, new))
        }
        queue.add(Quadruple(point, direction + Direction.EAST, distance + 1000, previous))
        queue.add(Quadruple(point, direction + Direction.WEST, distance + 1000, previous))
    }
    return 0
}

fun main() {
    val grid = Grid(16, false)
    val x = findP2(grid, grid.find { it == 'S' }!!, grid.find { it == 'E' }!!)
    val min = x.minBy { it!!.third }
    x.filter { it!!.third == min!!.third }.forEach { 
        it!!.fourth.forEach { 
            grid[it] = 'O'
        }
    }
    grid.print()
    grid.count { it == 'O' || it == 'S' || it == 'E' }.println()
}