package org.mccandless.minotaur.church

import org.junit.Test
import org.mccandless.minotaur.{CallByValue, TermMatchers}
import org.mccandless.minotaur.church.Booleans._
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitSuite

/**
  * Spec for algebra of church booleans.
  *
  * Purposefully doesn't extend the base test class so we can hide the `not` definition.
  *
  * Created by tdm on 12/3/17.
  */
class BooleansSpec extends JUnitSuite with TermMatchers with CallByValue {


  @Test
  def andSpec(): Unit = {
    tru and tru should beAlphaEquivalentTo (tru)
    tru and tru and tru should beAlphaEquivalentTo (tru)
    tru and fls should beAlphaEquivalentTo (fls)
    fls and tru should beAlphaEquivalentTo (fls)
    fls and fls should beAlphaEquivalentTo (fls)
  }


  @Test
  def orSpec(): Unit = {
    tru or tru should beAlphaEquivalentTo (tru)
    fls or tru should beAlphaEquivalentTo (tru)
    tru or fls should beAlphaEquivalentTo (tru)
    fls or fls should beAlphaEquivalentTo (fls)

  }

  @Test
  def notSpec(): Unit = {
    // hide the scalatest NotWord in scope from the base class
    import Booleans.not

    not(tru) should beAlphaEquivalentTo(fls)
    not(fls) should beAlphaEquivalentTo(tru)

    !tru should beAlphaEquivalentTo (fls)
    !fls should beAlphaEquivalentTo (tru)
  }
}
