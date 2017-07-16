package org.mccandless.minotaur.church

import org.mccandless.minotaur.{Lambda, Term, Var}

/**
  * Created by ergo on 7/16/17.
  */
object Booleans {

  val tru: Lambda = Lambda(Var("true"), Lambda(Var("false"), Var("true")))
  val fls: Lambda = Lambda(Var("true"), Lambda(Var("false"), Var("false")))
}
