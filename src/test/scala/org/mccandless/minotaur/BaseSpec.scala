package org.mccandless.minotaur

import org.scalatest.Matchers
import org.scalatest.junit.JUnitSuite

/**
  * Base class for all spec classes.
  *
  * Created by tdm on 12/3/17.
  */
abstract class BaseSpec extends JUnitSuite with Matchers with TermMatchers with CallByValue

trait CallByValue {
  implicit val reducer: Reducer = Implicits.callByValue
}