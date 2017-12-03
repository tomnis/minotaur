package org.mccandless.minotaur.church

import org.mccandless.minotaur.{Lambda, Term, Var}

/**
  * Created by ergo on 7/16/17.
  */
object Booleans {

  val tru: Term = Lambda(Var("x"), Lambda(Var("y"), Var("x")))
  val fls: Term = Lambda(Var("x"), Lambda(Var("y"), Var("y")))
}
