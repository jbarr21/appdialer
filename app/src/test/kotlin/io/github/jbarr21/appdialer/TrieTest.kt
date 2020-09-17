package io.github.jbarr21.appdialer

import com.google.common.truth.Truth.assertThat
import io.github.jbarr21.appdialer.util.Trie
import org.junit.Test

class TrieTest {

  private val appLabels = listOf("Joe", "John", "Johnny", "Jane", "Jack", "John Boy")
  private val trie = Trie<String>().apply {
    appLabels.forEach { this.add(it, it) }
  }

  @Test
  fun valueInTrie_returnsTrue() {
    assertThat("John" in trie).isTrue()
    assertThat("Johnny" in trie).isTrue()
    assertThat("Jack" in trie).isTrue()
  }

  @Test
  fun valueNotInTrie_returnsFalse() {
    assertThat("James" in trie).isFalse()
    assertThat("Jim" in trie).isFalse()
    assertThat("Jo" in trie).isFalse()
  }

  @Test
  fun partialValueInTrie_returnsTrue() {
    assertThat(trie.contains("Jo", partial = true)).isTrue()
  }

  @Test
  fun predictionsForJo_returnsJoeJohnJohnnyJohnBoy() {
    assertThat(trie.predictWord("Jo")).containsAllIn(listOf("Joe", "John", "Johnny", "John Boy"))
    assertThat(trie.predictWord("jo")).containsAllIn(listOf("Joe", "John", "Johnny", "John Boy"))
  }
}