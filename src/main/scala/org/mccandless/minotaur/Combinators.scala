package org.mccandless.minotaur

import org.mccandless.minotaur.church.Booleans

/**
  * Created by tomas.mccandless on 7/13/17.
  */
object Combinators {

  val id: Lambda = Lambda(Var("x"), Var("x"))

  val k: Lambda = Booleans.tru

  val omega: Lambda = Lambda(Var("x"), Apply(Var("x"), Var("x")))

  val Omega: Term = Apply(omega, omega)
}
