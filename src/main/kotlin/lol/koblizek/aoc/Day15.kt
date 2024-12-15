package lol.koblizek.aoc

import lol.koblizek.aoc.util.Grid
import lol.koblizek.aoc.util.print
import lol.koblizek.aoc.util.println
import lol.koblizek.aoc.util.readInput
import lol.koblizek.aoc.util.splitAtEmpty

enum class Instruction(val ch: Char) {
    LEFT('<'), RIGHT('>'), UP('^'), DOWN('v');
    
    fun opposite(): Instruction = when (this) {
        LEFT -> RIGHT
        RIGHT -> LEFT
        UP -> DOWN
        DOWN -> UP
    }
    
    fun toPosition(): Grid.Point = when (this) {
        LEFT -> Grid.Point(-1, 0)
        RIGHT -> Grid.Point(1, 0)
        UP -> Grid.Point(0, -1)
        DOWN -> Grid.Point(0, 1)
    }
    
    companion object {
        fun fromChar(ch: Char): Instruction = Instruction.entries.first { it.ch == ch }
    }
}

fun main() {
    val input = readInput(15, false).splitAtEmpty()
    val grid = Grid(input.first.map { it.toCharArray() }.toMutableList())
    val instructions = input.second.joinToString("")
    
    instructions.forEach {
        val instruction = Instruction.fromChar(it)
        val robot = grid.find { it == '@' }!!

        go(grid, robot, instruction)
    }
    grid.findAll { it == 'O' }.map { 
        it.y * 100 + it.x
    }.sum().println()
}

fun go(grid: Grid, robot: Grid.Point, instruction: Instruction) {
    var tempRobot = robot
    val boxes = mutableListOf<Grid.Point>()
    var canMove = false
    while (true) {
        tempRobot = tempRobot + instruction.toPosition()
        val cell = grid[tempRobot]
        if (cell == '#') {
            break
        } else if (cell == 'O') {
            boxes.add(tempRobot)
        } else {
            canMove = true
            break
        }
    }
    if (canMove) {
        boxes.asReversed().forEach { 
            grid[it + instruction.toPosition()] = 'O'
        }
        grid[robot] = '.'
        grid[robot + instruction.toPosition()] = '@'
    }
}