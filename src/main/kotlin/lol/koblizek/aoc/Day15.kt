package lol.koblizek.aoc

import lol.koblizek.aoc.util.Grid
import lol.koblizek.aoc.util.println
import lol.koblizek.aoc.util.readInput
import lol.koblizek.aoc.util.splitAtEmpty
import java.io.File

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
    d15p2()
}

fun d15p1() {
    val input = readInput(15, false).splitAtEmpty()
    val grid = Grid(input.first.map { it.toCharArray() }.toMutableList())
    val instructions = input.second.joinToString("")
    
    instructions.forEach {
        val instruction = Instruction.fromChar(it)
        val robot = grid.find { it == '@' }!!

        go1(grid, robot, instruction)
    }
    grid.findAll { it == 'O' }.map { 
        it.y * 100 + it.x
    }.sum().println()
}

fun d15p2() {
    val input = readInput(15, false).splitAtEmpty()
    val grid = remapGrid(Grid(input.first.map { it.toCharArray() }.toMutableList())) 
    val instructions = input.second.joinToString("")
    instructions.forEach {
        val instruction = Instruction.fromChar(it)
        val robot = grid.find { it == '@' }!!
        go2(grid, robot, instruction)
    }
    grid.findAll { it == '[' }.map { 
        it.y * 100 + it.x
    }.sum().println()
}

// point has to be [ or @ to work correctly
/*
Scenarios:
[]
[]
==
[][]
 []
==
 []
[]
==
[]
 []
*/
val boxesPositions = mutableListOf<Grid.Point>()
var isFucked = false
fun getBoxesAbove(grid: Grid, point: Grid.Point) {
    if (grid[point + Instruction.UP.toPosition()] == '[') {
        boxesPositions.add(point + Instruction.UP.toPosition())
        getBoxesAbove(grid, point + Instruction.UP.toPosition())
    } else if (grid[point + Instruction.UP.toPosition()] == ']') {
        if (!boxesPositions.contains(point + Instruction.UP.toPosition() + Instruction.LEFT.toPosition())) {
            boxesPositions.add(point + Instruction.UP.toPosition() + Instruction.LEFT.toPosition())
            getBoxesAbove(grid, point + Instruction.UP.toPosition() + Instruction.LEFT.toPosition())
        }
        if (grid[point + Instruction.UP.toPosition() + Instruction.RIGHT.toPosition()] == '['
            && !boxesPositions.contains(point + Instruction.UP.toPosition() + Instruction.RIGHT.toPosition())
            && grid[point] != '@') {
            boxesPositions.add(point + Instruction.UP.toPosition() + Instruction.RIGHT.toPosition())
            getBoxesAbove(grid, point + Instruction.UP.toPosition() + Instruction.RIGHT.toPosition())
        }
    } else if (grid[point] != '@'
        && grid[point + Instruction.RIGHT.toPosition() + Instruction.UP.toPosition()] == '[') {
        boxesPositions.add(point + Instruction.RIGHT.toPosition() + Instruction.UP.toPosition())
        getBoxesAbove(grid, point + Instruction.RIGHT.toPosition() + Instruction.UP.toPosition())
    }
}

fun getBoxesBelow(grid: Grid, point: Grid.Point) {
    if (grid[point + Instruction.DOWN.toPosition()] == '[') {
        boxesPositions.add(point + Instruction.DOWN.toPosition())
        getBoxesBelow(grid, point + Instruction.DOWN.toPosition())
    } else if (grid[point + Instruction.DOWN.toPosition()] == ']') {
        if (!boxesPositions.contains(point + Instruction.DOWN.toPosition() + Instruction.LEFT.toPosition())) {
            boxesPositions.add(point + Instruction.DOWN.toPosition() + Instruction.LEFT.toPosition())
            getBoxesBelow(grid, point + Instruction.DOWN.toPosition() + Instruction.LEFT.toPosition())
        }
        if (grid[point + Instruction.DOWN.toPosition() + Instruction.RIGHT.toPosition()] == '['
            && !boxesPositions.contains(point + Instruction.DOWN.toPosition() + Instruction.RIGHT.toPosition())
            && grid[point] != '@') {
            boxesPositions.add(point + Instruction.DOWN.toPosition() + Instruction.RIGHT.toPosition())
            getBoxesBelow(grid, point + Instruction.DOWN.toPosition() + Instruction.RIGHT.toPosition())
        }
    } else if (grid[point] != '@'
        && grid[point + Instruction.RIGHT.toPosition() + Instruction.DOWN.toPosition()] == '[') {
        boxesPositions.add(point + Instruction.RIGHT.toPosition() + Instruction.DOWN.toPosition())
        getBoxesBelow(grid, point + Instruction.RIGHT.toPosition() + Instruction.DOWN.toPosition())
    }
}

fun determineIfTopFucked(grid: Grid) {
    boxesPositions.map { 
        listOf(it, it + Instruction.RIGHT.toPosition())
    }.flatMap { it }.forEach {
        if (grid[it + Instruction.UP.toPosition()] == '#') {
            isFucked = true
        }
    }
}

fun determineIfBottomFucked(grid: Grid) {
    boxesPositions.map {
        listOf(it, it + Instruction.RIGHT.toPosition())
    }.flatMap { it }.forEach {
        if (grid[it + Instruction.DOWN.toPosition()] == '#') {
            isFucked = true
        }
    }
}

fun go2(grid: Grid, robot: Grid.Point, instruction: Instruction) {
    var tempRobot = robot
    if (instruction == Instruction.LEFT || instruction == Instruction.RIGHT) {
        val boxes = mutableListOf<Grid.Point>()
        var canMove = false
        while (true) {
            tempRobot = tempRobot + instruction.toPosition()
            val cell = grid[tempRobot]
            if (cell == '#') {
                break
            } else if ("[]".contains(cell)) {
                boxes.add(tempRobot)
            } else {
                canMove = true
                break
            }
        }
        if (canMove) {
            boxes.asReversed().forEach {
                grid[it + instruction.toPosition()] = grid[it]
            }
            grid[robot] = '.'
            grid[robot + instruction.toPosition()] = '@'
        }
    } else if (instruction == Instruction.UP) {
        getBoxesAbove(grid, robot)
        determineIfTopFucked(grid)
        if (!isFucked && grid[robot + Instruction.UP.toPosition()] != '#') {
            boxesPositions.forEach { 
                grid[it] = '.'
                grid[it + Instruction.RIGHT.toPosition()] = '.'
            }
            boxesPositions.map { it + Instruction.UP.toPosition() }.forEach {
                grid[it] = '['
                grid[it + Instruction.RIGHT.toPosition()] = ']'
            }
            grid[robot] = '.'
            if (grid[robot + Instruction.UP.toPosition()] == '[') {
                grid[robot + Instruction.UP.toPosition() + Instruction.RIGHT.toPosition()] = '.'
            } else if (grid[robot + Instruction.UP.toPosition()] == ']') {
                grid[robot + Instruction.UP.toPosition() + Instruction.LEFT.toPosition()] = '.'
            }
            grid[robot + Instruction.UP.toPosition()] = '@'
        }
        isFucked = false
        boxesPositions.clear()
    } else if (instruction == Instruction.DOWN) {
        getBoxesBelow(grid, robot)
        determineIfBottomFucked(grid)
        if (!isFucked && grid[robot + Instruction.DOWN.toPosition()] != '#') {
            boxesPositions.forEach {
                grid[it] = '.'
                grid[it + Instruction.RIGHT.toPosition()] = '.'
            }
            boxesPositions.map { it + Instruction.DOWN.toPosition() }.forEach {
                grid[it] = '['
                grid[it + Instruction.RIGHT.toPosition()] = ']'
            }
            grid[robot] = '.'
            if (grid[robot + Instruction.DOWN.toPosition()] == '[') {
                grid[robot + Instruction.DOWN.toPosition() + Instruction.RIGHT.toPosition()] = '.'
            } else if (grid[robot + Instruction.DOWN.toPosition()] == ']') {
                grid[robot + Instruction.DOWN.toPosition() + Instruction.LEFT.toPosition()] = '.'
            }
            grid[robot + Instruction.DOWN.toPosition()] = '@'
        }
        isFucked = false
        boxesPositions.clear()
    }
    println()
}

fun remapGrid(grid: Grid): Grid {
    return Grid(grid.input.map { 
        it.map {
            when (it) {
                '#' -> "##"
                'O' -> "[]"
                '@' -> "@."
                else -> ".."
            }.toList()
        }.flatMap { it }.toCharArray()
    }.toMutableList())
}

fun go1(grid: Grid, robot: Grid.Point, instruction: Instruction) {
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