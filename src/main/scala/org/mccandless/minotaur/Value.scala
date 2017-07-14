package org.mccandless.minotaur

/**
  * Created by tomas.mccandless on 7/13/17.
  */
sealed trait Value

case class Num(n: Int) extends Value

case class Bool(b: Boolean) extends Value

