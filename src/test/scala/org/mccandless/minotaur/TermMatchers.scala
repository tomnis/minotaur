package org.mccandless.minotaur

import org.scalatest.matchers.{BeMatcher, MatchResult, Matcher}
import org.scalatest.words.ResultOfNotWordForAny

trait TermMatchers {

  class AlphaEquivalenceMatcher(expectedTerm: Term) extends Matcher[Term] {

    override def apply(left: Term) = {
      MatchResult(
        left.isAlphaEquivalentTo(expectedTerm),
        s"""$left was not alpha-equivalent to $expectedTerm""",
        s"""$left was alpha-equivalent to $expectedTerm"""
      )
    }
  }

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
