package lol.koblizek.aoc

import lol.koblizek.aoc.util.println
import lol.koblizek.aoc.util.readInput

fun runReplace(list: MutableList<Long>): MutableList<Long> {
    val newList = mutableListOf<Long>()
    list.forEach { 
        if (it == 0L) {
            newList.add(1)
        } else if (it.toString().length % 2 == 0) {
            val str = it.toString()
            val half = str.length / 2
            newList.add(str.substring(0, half).toLong())
            newList.add(str.substring(half).toLong())
        } else {
            newList.add(it * 2024)
        }
    }
    return newList
}

fun splitNumberInHalf(num: Long): Pair<Long, Long> {
    val str = num.toString()
    val half = str.length / 2
    return str.substring(0, half).toLong() to str.substring(half).toLong()
}

fun MutableMap<Long, Long>.startOrIncrement(key: Long, value: Long) {
    if (!contains(key))
        put(key, value)
    else computeIfPresent(key) { k, v -> v + value }
}

fun runReplace2(map: MutableMap<Long, Long>): MutableMap<Long, Long> {
    val newMap: MutableMap<Long, Long> = mutableMapOf()
    map.forEach { (key, value) ->
        if (key == 0L) {
            newMap.startOrIncrement(1, value)
        } else if (key.toString().length % 2 == 0) {
            val (first, second) = splitNumberInHalf(key)
            newMap.startOrIncrement(first, value)
            newMap.startOrIncrement(second, value)
        } else {
            newMap.startOrIncrement(key * 2024, value)
        }
    }
    return newMap
}

fun createDefaultMap(list: List<Long>): MutableMap<Long, Long> {
    return list.map { it to 1L }.toMap().toMutableMap()
}

fun main() {
    var input = readInput(11, false)[0].split(" ").map { it.toLong() }.toMutableList()
    var map = createDefaultMap(input)
    
    for (i in 1..75) {
        // input = runReplace(input)
        map = runReplace2(map)
    }
    // input.size.println()
    map.map { it.value }.sum().println()
}