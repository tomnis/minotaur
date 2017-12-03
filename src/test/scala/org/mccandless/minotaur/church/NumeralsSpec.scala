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
  def add(): Unit = {

    one + one should beAlphaEquivalentTo (two)
    one + two should beAlphaEquivalentTo (three)
    two + two should beAlphaEquivalentTo (four)
  }
}
