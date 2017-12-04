package org.mccandless.minotaur

import org.scalatest.matchers.{BeMatcher, MatchResult, Matcher}
import org.scalatest.words.ResultOfNotWordForAny

trait TermMatchers {

  /**
    * Matches when the left (actual value) is a [[Term]].
    * @param expectedTerm
    */
  class AlphaEquivalenceMatcher(expectedTerm: Term) extends Matcher[Term] {

    override def apply(left: Term) = {
      MatchResult(
        left.isAlphaEquivalentTo(expectedTerm),
        s"""$left was not alpha-equivalent to $expectedTerm""",
        s"""$left was alpha-equivalent to $expectedTerm"""
      )
    }
  }


  /**
    * Matches when the left (actual value) is a [[DeBruijnTerm]].
    *
    * @param expectedTerm
    */
  class DeBruijnAlphaEquivalenceMatcher(expectedTerm: Term) extends Matcher[DeBruijnTerm] {

    override def apply(left: DeBruijnTerm) = {
      MatchResult(
        left == expectedTerm.toDeBruijn,
        s"""$left was not alpha-equivalent to $expectedTerm""",
        s"""$left was alpha-equivalent to $expectedTerm"""
      )
    }
  }

  // TODO think about some nicer syntax here. =𝛂= ?
  def beAlphaEquivalentTo(expectedTerm: Term) = new AlphaEquivalenceMatcher(expectedTerm)



  implicit class NegatedAlphaEquivalenceMatcher[T <: Term](t: ResultOfNotWordForAny[T]) {
    def beAlphaEquivalentTo(expectedTerm: Term) = {
      this.t be BeMatcher[Term] {
        left => MatchResult(!left.isAlphaEquivalentTo(expectedTerm), "", "")
      }
    }
  }
}


object TermMatchers extends TermMatchers
