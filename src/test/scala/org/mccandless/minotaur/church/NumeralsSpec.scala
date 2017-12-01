package org.mccandless.minotaur.church

import org.junit.{Ignore, Test}
import org.mccandless.minotaur.{Apply, TermMatchers}
import org.mccandless.minotaur.church.Numerals._
import org.scalatest.Matchers
import org.scalatest.junit.JUnitSuite

/**
  * Created by ergo on 7/16/17.
  */
class NumeralsSpec extends JUnitSuite with Matchers with TermMatchers {


  /** Checks that we can apply the successor function. */
  @Test
  def succSpec(): Unit = {
    Apply(succ, zero).reduce should beAlphaEquivalentTo (one)
    Apply(succ, one).reduce should beAlphaEquivalentTo (two)
    Apply(succ, two).reduce should beAlphaEquivalentTo (three)
  }


  @Test
  def add(): Unit = {

    Numerals.plus(zero, one).reduce should beAlphaEquivalentTo (one)
    Numerals.plus(one, one).reduce should beAlphaEquivalentTo (two)
//
    Numerals.plus(one, two).reduce should beAlphaEquivalentTo (three)
  }
}
