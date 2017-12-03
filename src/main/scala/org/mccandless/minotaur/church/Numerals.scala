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


  /**
    * The successor expression.
    *
    * Applies f n times, then applies f one more time.
    */
  val succ: Term = Lambda(Var("n"),
    Lambda(Var("f"),
      Lambda(Var("x"),
        Apply(Var("f"),
          Apply( Apply(Var("n"),Var("f")) , Var("x") )
        )
      )
    )
  )

  /**
    * Applies the successor function ([[succ]]) to `term`.
    *
    * @param term number to be incremented.
    * @return the successor of `term`.
    */
  def succ(term: Term): Term = Apply(succ, term).reduce

  /**
    * The addition expression.
    *
    * Applies f n times, then m more times.
    */
  val + : Term = Lambda(Var("m"), Lambda(Var("n"), Lambda(Var("f"), Lambda(Var("x"),
    Apply(
      Apply(Var("m"), Var("f")),
      Apply(
        Apply(Var("n"), Var("f")),
        Var("x")
      )
    )
  ))))


  // allows use of arithmetic as infix operators on terms
  implicit class ChurchArithmetic(val term: Term) extends ArithmeticOperators
}



trait ArithmeticOperators {

  val term: Term

  def +(that: Term): Term = Apply(Apply(Numerals.+, this.term), that).reduce
  def -(that: Term): Term = ???
  def *(that: Term): Term = ???
  def /(that: Term): Term = ???
}