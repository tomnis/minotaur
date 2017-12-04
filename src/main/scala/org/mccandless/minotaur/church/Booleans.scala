package org.mccandless.minotaur.church

import org.mccandless.minotaur.{Apply, Lambda, Term, Var}

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
    def apply(x: Term): Term = Apply(notExpr, x).reduce
  }

  object isZero extends Expressions {
    def apply(x: Term): Term = ???
  }
}



private[church] trait Expressions {

  val andExpr: Term = Lambda(Var("x"), Lambda(Var("y"), Apply(Apply(Var("x"), Var("y")), Var("x"))))
  val orExpr: Term = Lambda(Var("x"), Lambda(Var("y"), Apply(Apply(Var("x"), Var("x")), Var("y"))))
  val notExpr: Term = Lambda(Var("x"), Apply(Apply(Var("x"), Booleans.fls), Booleans.tru))
//  val leqExpr: Term = ???
//  val ifThenElseExpr: Term = ???
}


private[church] trait Operators extends Expressions {

  protected val x: Term

  def and(y: Term): Term = Apply(Apply(andExpr, this.x), y).reduce

  def or(y: Term): Term = Apply(Apply(orExpr, this.x), y).reduce

  def leq(y: Term): Term = ???

  def ifThenElse(p: Term, a: Term, b: Term): Term = ???

  // advanced terse syntax
  // TODO think about how to separate this. it might be neat if it were possible to pick between the two syntaxes
  // just by switching an import
  def unary_! : Term = Booleans.not(this.x)
  def /\(y: Term): Term = this and y
  def \/(y: Term): Term = this or y
  def &&(y: Term): Term = this and y
  def ||(y: Term): Term = this or y

  def <=(y: Term): Term = this leq y
}