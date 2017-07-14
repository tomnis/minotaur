package org.mccandless.minotaur

/**
  * Created by tomas.mccandless on 7/13/17.
  */
object Constants {

  val id: Lambda = Lambda(Var("x"), Var("x"))

  val tru: Term = Lambda(Var("true"), Lambda(Var("false"), Var("true")))
  val fls: Term = Lambda(Var("true"), Lambda(Var("false"), Var("false")))
}
