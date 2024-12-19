package lol.koblizek.aoc

import lol.koblizek.aoc.util.println
import lol.koblizek.aoc.util.readInput

data class TrieNode(var children: MutableMap<Char, TrieNode> = mutableMapOf(), var isEnd: Boolean = false)

class Trie {
    val root = TrieNode()
    
    fun insert(word: String) {
        var node = root
        for (char in word) {
            node = node.children.getOrPut(char) { TrieNode() }
        }
        node.isEnd = true
    }
    
    fun search(word: String, start: Long = 0): MutableList<Long> {
        var result = mutableListOf<Long>()
        var node = root
        for (i in start until word.length) {
            val char = word[i.toInt()]
            if (char !in node.children) break
            node = node.children[char]!!
            if (node.isEnd) {
                result.add(i + 1)
            }
        }
        return result
    }
    
    fun countWays(design: String, memo: MutableMap<Long, Long>, start: Long = 0): Long {
        if (memo.containsKey(start)) {
            return memo[start]!!
        }
        if (start == design.length.toLong()) {
            return 1
        }
        val matching = search(design, start)
        val result = matching.map {
            countWays(design, memo, it)
        }.sum()
        memo[start] = result
        return result
    }
}

fun main() {
    val read = readInput(19, false)
    val patterns = read[0].split(", ")
    val trie = Trie()
    for (pattern in patterns) {
        trie.insert(pattern)
    }
    val designs = read.subList(2, read.size)
    designs.map { trie.countWays(it, mutableMapOf()) }.sum().println()
}