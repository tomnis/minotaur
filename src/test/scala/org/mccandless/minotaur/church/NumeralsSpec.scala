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


  @Test
  def succSpec(): Unit = {
    Apply(succ, zero).reduce should beAlphaEquivalentTo (one)
  }


  @Test
  @Ignore
  def add(): Unit = {

    Numerals.+(zero, one) should be (one)
    Numerals.+(one, one) should be (two)
  }
}
