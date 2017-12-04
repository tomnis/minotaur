package org.mccandless.minotaur.sugar

import org.mccandless.minotaur._

/**
  *
  * Created by tdm on 12/3/17.
  */
object Syntax {

  object let {
    def apply(v: Var): ResultOfLet = ResultOfLet(v)
    def apply(v: String): ResultOfLet = this(Var(v))
  }


  object letrec {
    def apply(v: Var): ResultOfLet = ResultOfLet(v)
    def apply(v: String): ResultOfLet = this(Var(v))
  }
}

case class ResultOfLet(v: Var) {
  def equal(m: Term) = ResultOfEqual(v, m)
}
case class ResultOfEqual(v: Var, m: Term) {
  def in(n: Term): Term = Apply(Lambda(v,n), m)
}
