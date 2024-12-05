package lol.koblizek.aoc

import lol.koblizek.aoc.util.readInput
import java.util.function.Predicate
import kotlin.collections.forEachIndexed

fun <T> split(list: List<T>, predicate: Predicate<T>): Pair<List<T>, List<T>> {
    val first = mutableListOf<T>()
    val second = mutableListOf<T>()
    var target = first
    for (item in list) {
        if (predicate.test(item)) {
            target = second
        } else {
            target.add(item)
        }
    }
    return Pair(first, second)
}

fun <T> Iterable<T>.allNotEmpty(predicate: (T) -> Boolean): Boolean {
    if (!this.any()) return false
    return all(predicate)
}

fun main() {
    val input = readInput(5, false)
    val (first, second) = split(input) { it.isEmpty() }
    
    val rules = first.map {
        val x = it.split("|").map { it.toInt() }
        Pair(x.first(), x[1])
    }.toHashSet()
    val data = second.map { it.split(",") }.map { it.map { it.toInt() } }
    
    var sum = 0
    data.forEach { line ->
        fun getWrong(l: List<Int>): List<Pair<Int, Int>> {
            val x = mutableListOf<Pair<Int, Int>>()
            l.forEachIndexed { i, p ->
                x.addAll(rules.filter { it.first == p && l.indexOf(it.second) != -1 }.filter { l.indexOf(it.second) < i }.map { Pair(l.indexOf(it.second), i) })
                x.addAll(rules.filter { it.second == p && l.indexOf(it.first) != -1 }.filter { l.indexOf(it.first) > i }.map { Pair(i, l.indexOf(it.first)) })
            }
            return x.distinct()
        }
        
        var newLine = line.toMutableList()
        var x = getWrong(newLine)
        if (x.isNotEmpty()) {
            while (x.isNotEmpty()) {
                x.forEach {
                    val temp = newLine[it.first]
                    newLine[it.first] = newLine[it.second]
                    newLine[it.second] = temp
                }
                x = getWrong(newLine)
            }

            sum += (newLine[newLine.size / 2])
        }
    }
    println(sum)
}