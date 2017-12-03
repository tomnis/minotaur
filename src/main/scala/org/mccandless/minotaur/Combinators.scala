package org.mccandless.minotaur

import org.mccandless.minotaur.church.Booleans

/**
  * Created by tomas.mccandless on 7/13/17.
  */
object Combinators {

  val id: Term = Lambda(Var("x"), Var("x"))

  val k: Term = Booleans.tru

  val omega: Term = Lambda(Var("x"), Apply(Var("x"), Var("x")))
}
