package org.mccandless.minotaur.church

import org.mccandless.minotaur.{Apply, Lambda, Term, Var}

/**
  * Church encodings for natural numbers.
  *
  * Created by ergo on 7/16/17.
  */
object Numerals {

  // interestingly, zero has the same encoding as false
  val zero: Term = Booleans.fls
  val one: Term = Lambda(Var("x"), Lambda(Var("y"), Apply(Var("x"), Var("y"))))
  val two: Term = Lambda(Var("x"), Lambda(Var("y"), Apply(Var("x"), Apply(Var("x"), Var("y")))))
  val three: Term = Lambda(Var("x"), Lambda(Var("y"), Apply(Var("x"), Apply(Var("x"), Apply(Var("x"), Var("y"))))))
  val four: Term = Lambda(Var("x"), Lambda(Var("y"), Apply(Var("x"),Apply(Var("x"), Apply(Var("x"), Apply(Var("x"), Var("y")))))))


  // allows use of arithmetic as infix operators on terms
  implicit class Arithmetic(val n: Term) extends ArithmeticOperators

  object succ extends ArithmeticExpressions {
    def apply(n: Term): Term = Apply(succExpr, n).reduce
  }
}


private[church] trait ArithmeticExpressions {
  /**
    * The successor expression.
    *
    * Applies f n times, then applies f one more time.
    */
  val succExpr: Term = Lambda(Var("n"),
    Lambda(Var("f"),
      Lambda(Var("x"),
        Apply(Var("f"),
          Apply( Apply(Var("n"),Var("f")) , Var("x") )
        )
      )
    )
  )

  /**
    * The addition expression.
    *
    * Applies f n times, then m more times.
    */
  val addExpr : Term = Lambda(Var("m"), Lambda(Var("n"), Lambda(Var("f"), Lambda(Var("x"),
    Apply(
      Apply(Var("m"), Var("f")),
      Apply(
        Apply(Var("n"), Var("f")),
        Var("x")
      )
    )
  ))))
}


private[church] trait ArithmeticOperators extends ArithmeticExpressions {

  val n: Term

  def +(m: Term): Term = Apply(Apply(addExpr, n), m).reduce
  def -(m: Term): Term = ???
  def *(m: Term): Term = ???
  def /(m: Term): Term = ???
}