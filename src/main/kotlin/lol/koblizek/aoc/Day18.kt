package lol.koblizek.aoc

import lol.koblizek.aoc.util.Grid
import lol.koblizek.aoc.util.readInput
import java.util.LinkedList
import java.util.Queue

fun bfs(grid: Grid): Int {
    val start = Grid.Point(0, 0)
    val end = Grid.Point(70, 70)
    val visited = mutableListOf<Grid.Point>()
    val queue: Queue<Pair<Grid.Point, Int>> = LinkedList()
    
    queue.add(start to 0)
    visited.add(start)
    
    while (queue.isNotEmpty()) {
        val (current, weight) = queue.poll()
        val neighbors = grid.getNeighbours(current)
        
        if (current == end) {
            println(weight)
            return weight
        }
        
        for (neighbor in neighbors) {
            if (neighbor !in visited && grid[neighbor] != '#') {
                queue.add(neighbor to weight + 1)
                visited.add(neighbor)
            }
        }
    }
    return -1
}

fun main() {
    val pairs = readInput(18, false).map { it.split(",") }.map { it[0].toInt() to it[1].toInt() }
    val grid = Grid(71, 71)
    
    pairs.subList(0, 1024).forEach {
        grid[Grid.Point(it.first, it.second)] = '#'
    }
    grid.print()
    bfs(grid)
}