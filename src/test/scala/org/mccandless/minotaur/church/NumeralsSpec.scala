package org.mccandless.minotaur.church

import org.junit.Test
import org.mccandless.minotaur.BaseSpec
import org.mccandless.minotaur.church.Numerals._

/**
  * Created by ergo on 7/16/17.
  */
class NumeralsSpec extends BaseSpec {


  /** Checks that we can apply the successor function. */
  @Test
  def succSpec(): Unit = {
    succ(zero) should beAlphaEquivalentTo (one)
    succ(one) should beAlphaEquivalentTo (two)
    succ(two) should beAlphaEquivalentTo (three)
    succ(three) should beAlphaEquivalentTo (four)
  }


  @Test
  def predSpec(): Unit = {
    pred(four) should beAlphaEquivalentTo(three)
    pred(three) should beAlphaEquivalentTo(two)
    pred(two) should beAlphaEquivalentTo(one)
    pred(one) should beAlphaEquivalentTo(zero)
  }


  @Test
  def add(): Unit = {
    zero + zero should beAlphaEquivalentTo(zero)
    one + zero should beAlphaEquivalentTo(one)
    one + one should beAlphaEquivalentTo (two)
    one + two should beAlphaEquivalentTo (three)
    two + two should beAlphaEquivalentTo (four)
  }

  @Test
  def subtract(): Unit = {
    zero - zero should beAlphaEquivalentTo(zero)
    one - one should beAlphaEquivalentTo(zero)

    three - one should beAlphaEquivalentTo(two)
    four - three should beAlphaEquivalentTo(one)

    four - four should beAlphaEquivalentTo(zero)
  }


  @Test
  def multiply(): Unit = {
    one * one should beAlphaEquivalentTo(one)
    two * two should beAlphaEquivalentTo(four)
  }


  @Test
  def power(): Unit = {
    one ** four should beAlphaEquivalentTo(one)
    two ** two should beAlphaEquivalentTo(four)
    four ** one should beAlphaEquivalentTo(four)
  }
}
