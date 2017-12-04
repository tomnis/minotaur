package org.mccandless.minotaur

import org.junit.Test
import org.mccandless.minotaur.church.Numerals._
import org.mccandless.minotaur.Control.fix
import org.pmw.tinylog.Logger

/**
  *
  * Created by tdm on 12/3/17.
  */
class ControlSpec extends BaseSpec {

  @Test
  def yFixedPoint(): Unit = {

    val f = fix(zero)
    Logger.info(f)
  }


  @Test
  def factFixedPoint(): Unit = {

  }
}
