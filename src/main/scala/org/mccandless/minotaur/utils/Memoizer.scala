package org.mccandless.minotaur.utils

/**
  * Memoizes functions for more efficient evaluation.
  *
  * Created by tdm on 12/3/17.
  */
class Memoizer[A, B] {
  import scala.collection.mutable
  val cache: mutable.Map[A, B] = mutable.Map.empty

  def memoize(f: A => B): A => B = {
    (a: A) => this.cache.getOrElseUpdate(a, f(a))
  }
}
