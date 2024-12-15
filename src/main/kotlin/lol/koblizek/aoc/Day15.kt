package lol.koblizek.aoc

import lol.koblizek.aoc.util.Grid
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
fun getBoxes(grid: Grid, point: Grid.Point, instruction: Instruction) {
    when {
        grid[point + instruction.toPosition()] == '[' -> {
            boxesPositions.add(point + instruction.toPosition())
            getBoxes(grid, point + instruction.toPosition(), instruction)
        }
        grid[point + instruction.toPosition()] == ']' -> {
            if (!boxesPositions.contains(point + instruction.toPosition() + Instruction.LEFT.toPosition())) {
                boxesPositions.add(point + instruction.toPosition() + Instruction.LEFT.toPosition())
                getBoxes(grid, point + instruction.toPosition() + Instruction.LEFT.toPosition(), instruction)
            }
            if (grid[point + instruction.toPosition() + Instruction.RIGHT.toPosition()] == '['
                && !boxesPositions.contains(point + instruction.toPosition() + Instruction.RIGHT.toPosition())
                && grid[point] != '@') {
                boxesPositions.add(point + instruction.toPosition() + Instruction.RIGHT.toPosition())
                getBoxes(grid, point + instruction.toPosition() + Instruction.RIGHT.toPosition(), instruction)
            }
        }
        grid[point] != '@'
                && grid[point + Instruction.RIGHT.toPosition() + instruction.toPosition()] == '[' -> {
            boxesPositions.add(point + Instruction.RIGHT.toPosition() + instruction.toPosition())
            getBoxes(grid, point + Instruction.RIGHT.toPosition() + instruction.toPosition(), instruction)
        }
    }
}

fun determineIfFucked(grid: Grid, instruction: Instruction) {
    boxesPositions.map { 
        listOf(it, it + Instruction.RIGHT.toPosition())
    }.flatMap { it }.forEach {
        if (grid[it + instruction.toPosition()] == '#') {
            isFucked = true
        }
    }
}

fun go2(grid: Grid, robot: Grid.Point, instruction: Instruction) {
    var tempRobot = robot
    when (instruction) {
        Instruction.LEFT, Instruction.RIGHT -> {
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
        }
        Instruction.UP -> {
            getBoxes(grid, robot, instruction)
            determineIfFucked(grid, instruction)
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
        }
        Instruction.DOWN -> {
            getBoxes(grid, robot, instruction)
            determineIfFucked(grid, instruction)
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
    }
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