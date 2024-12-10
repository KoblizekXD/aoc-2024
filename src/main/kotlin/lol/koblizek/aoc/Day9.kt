package lol.koblizek.aoc

import lol.koblizek.aoc.util.printStrings
import lol.koblizek.aoc.util.println
import lol.koblizek.aoc.util.readInput

val digits = "0123456789".toCharArray()

fun p1() {
    val input = readInput(9, false)[0]
    println(input.length)
    var res = input.mapIndexedNotNull { i, c ->
        if (i % 2 == 0) {
            ((i / 2).toString() + " ").repeat(c.digitToInt()).trim()
        } else ".".repeat(c.digitToInt())
    }.flatMap { if (!it.contains(".")) it.split(" ") else it.toCharArray().map { it.toString() } }
        .filter { it.isNotEmpty() }
        .toMutableList()
    println(res)
    while (res.subList(0, res.indexOfLast { it.toIntOrNull() != null } + 1).any { it[0] == '.' }) {
        val replacingIndex = res.indexOfLast { it.toIntOrNull() != null }
        val number = res[replacingIndex]
        res.indexOf(".").let { res[it] = number }
        res[replacingIndex] = "."
    }
    println(res)
    res.subList(0, res.indexOf(".")).mapIndexed { i, it ->
        i * it.toLong()
    }.sum().println()
}

fun findStartingIndexOf(array: List<String>, endIndex: Int): Int {
    var index = endIndex
    while (index > -1 && array[index] == array[endIndex]) {
        index--
    }
    return index
}

fun findConsecutive(array: List<String>, count: Int): Int {
    var counter = 0
    array.withIndex().forEach { 
        if (it.value == ".") counter++
        else counter = 0
        if (counter == count) {
            return it.index - count + 1
        }
    }
    return -1
}

fun findAllParts(arr: List<String>): List<Pair<Int, Int>> {
    val res = mutableListOf<Pair<Int, Int>>()
    var counter = 0
    var c: String = "."
    arr.withIndex().forEach {
        if (it.value == c) {
            counter++
        } else {
            if (counter != 0) {
                res.add(it.index - counter to it.index - 1)
            }
            counter = 1
            c = it.value
        }
    }
    if (counter != 0) {
        res.add(arr.size - counter to arr.size - 1)
    }
    return res
}

fun p2() {
    val input = readInput(9, false)[0]
    var res = input.mapIndexedNotNull { i, c ->
        if (i % 2 == 0) {
            ((i / 2).toString() + " ").repeat(c.digitToInt()).trim()
        } else ".".repeat(c.digitToInt())
    }.flatMap { if (!it.contains(".")) it.split(" ") else it.toCharArray().map { it.toString() } }
        .filter { it.isNotEmpty() }
        .toMutableList()
    val parts = findAllParts(res)
    parts.reversed().forEachIndexed { _, pair -> 
        val length = pair.second - pair.first + 1
        findConsecutive(res.subList(0, pair.first), length).let {
            if (it != -1) {
                for (j in 0 until length) {
                    res[it + j] = res[pair.first + j]
                    res[pair.first + j] = "."
                }
            }
        }
    }
    res.mapIndexedNotNull { i, it ->
        val index = it.toLongOrNull()
        if (index != null) index * i else null
    }.sum().println()
}

fun main() {
    p2()
}