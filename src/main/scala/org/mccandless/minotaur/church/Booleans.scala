package org.mccandless.minotaur.church

import org.mccandless.minotaur._

/**
  * Church encodings for booleans and their associated algebraic operators.
  *
  * Created by ergo on 7/16/17.
  */
object Booleans {

  val tru: Term = Lambda(Var("x"), Lambda(Var("y"), Var("x")))
  val fls: Term = Lambda(Var("x"), Lambda(Var("y"), Var("y")))

  implicit class BooleanAlgebra(val x: Term) extends Operators

  object not extends Expressions {
    def apply(x: Term)(implicit r: Reducer): Term = r(Apply(notExpr, x))
  }

  object isZero extends Expressions {
    def apply(x: Term)(implicit r: Reducer): Term = ???
  }

  // TODO might want to move to control object instead of boolean
  object ifThenElse extends Expressions {
    def apply(p: Term, a: Term, b: Term)(implicit r: Reducer): Term = r(Apply(Apply(Apply(ifThenElseExpr, p), a), b))
  }
}



private[church] trait Expressions extends ArithmeticExpressions {

  val andExpr: Term = Lambda(Var("x"), Lambda(Var("y"), Apply(Apply(Var("x"), Var("y")), Var("x"))))
  val orExpr: Term = Lambda(Var("x"), Lambda(Var("y"), Apply(Apply(Var("x"), Var("x")), Var("y"))))
  val notExpr: Term = Lambda(Var("x"), Apply(Apply(Var("x"), Booleans.fls), Booleans.tru))


  /**
    * λn.n (λx.FALSE) TRUE
    */
  val isZeroExpr: Term = Lambda("n", Apply(Apply("n", Lambda("x", Booleans.fls)), Booleans.tru))

  /**
    * Less-than-or-equal
    *
    * λm.λn.ISZERO (SUB m n)
    */
  val leqExpr: Term = Lambda("m", Lambda("n", Apply(isZeroExpr, Apply(Apply(subtractExpr, "m"), "n"))))


  /**
    * If-then-else
    *
    * λp.λa.λb.p a b
    */
  val ifThenElseExpr: Term = Lambda("p", Lambda("a", Lambda("b", Apply(Apply("p", "a"), "b"))))
}


private[church] trait Operators extends Expressions {

  protected val x: Term

  def and(y: Term)(implicit r: Reducer): Term = r(Apply(Apply(andExpr, this.x), y))

  def or(y: Term)(implicit r: Reducer): Term = r(Apply(Apply(orExpr, this.x), y))

  def leq(y: Term)(implicit r: Reducer): Term = r(Apply(Apply(leqExpr, this.x), y))

  // advanced terse syntax
  // TODO think about how to separate this. it might be neat if it were possible to pick between the two syntaxes
  // just by switching an import
  def unary_!(implicit r: Reducer) : Term = Booleans.not(this.x)
  def /\(y: Term)(implicit r: Reducer): Term = this and y
  def \/(y: Term)(implicit r: Reducer): Term = this or y
  def &&(y: Term)(implicit r: Reducer): Term = this and y
  def ||(y: Term)(implicit r: Reducer): Term = this or y

  def <=(y: Term)(implicit r: Reducer): Term = this leq y
}