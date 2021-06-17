package io.github.jbarr21.appdialer.util

import io.github.jbarr21.appdialer.ui.main.dialer.DialerModule
import java.util.*

// TODO: support matching at more than just the first letter of the label
class Trie<T>(
  val root: TrieNode<T> = TrieNode(
    ' '
  )
) {

  private val keyMappings = DialerModule.keyMappings()

  fun add(word: String, value: T? = null) {
    var node = root
    val sanitizedWord = sanitize(word)
    sanitizedWord.forEach { ch ->
      node = node.children.firstOrNull { it.key == ch } ?: TrieNode<T>(ch.toLowerCase()).also { node.children += it }
    }
    node.isLeaf = true
    node.value = value
  }

  fun contains(word: String, partial: Boolean = false): Boolean {
    var node = root
    sanitize(word).forEach { ch: Char ->
      node = node.children.firstOrNull { it.key == ch } ?: return false
    }
    return node.isLeaf || partial
  }

  fun predictWord(word: String): List<T> {
    var node = root
    if (word.isNotEmpty()) {
      sanitize(word).forEach { ch: Char ->
        node = node.children.firstOrNull { it.key == ch } ?: return emptyList()
      }
    }
    return bfs(node)
  }

  fun clear() {
    root.children.clear()
  }

  fun bfs(root: TrieNode<T>): List<T> {
    val list = mutableListOf<T>()
    val queue = LinkedList<TrieNode<T>>()
    queue += root
    while (queue.isNotEmpty()) {
      val node = queue.remove()
      if (node.isLeaf) {
        node.value?.let { list += it }
      }
      queue.addAll(node.children)
    }
    return list
  }

  private fun sanitize(word: String): String {
    return word.map { it.toLowerCase() }.filter { it in 'a'..'z' }.map { digitize(it) }.joinToString(separator = "")
  }

  private fun digitize(ch: Char): String {
    return keyMappings.entries.firstOrNull {
        (_, letters) -> letters.any { it == ch }
    }?.key?.toString() ?: throw IllegalStateException("No match for $ch")
  }

  operator fun Trie<T>.plus(word: String) = add(word, value = null)
  operator fun Trie<T>.plusAssign(word: String) = add(word, value = null)
  operator fun contains(word: String): Boolean = contains(word, partial = false)

  data class TrieNode<T>(
    val key: Char,
    var value: T? = null,
    var isLeaf: Boolean = false,
    val children: MutableList<TrieNode<T>> = mutableListOf()
  )
}