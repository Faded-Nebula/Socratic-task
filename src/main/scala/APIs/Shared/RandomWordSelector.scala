package APIs.Shared

import scala.util.Random

object RandomWordSelector {
  // The word set
  private val words: List[String] = List("Alice", "Bob", "Carol", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy", "Mike", "Ninja", "Oscar", "Pat", "Quark", "Rupert", "Sybil", "Ted", "Uber", "Victor", "Walter")
  // The method to choose a random word
  def selectRandomWord(): String = {
    val randomIndex = Random.nextInt(words.length)
    words(randomIndex)
  }
}

