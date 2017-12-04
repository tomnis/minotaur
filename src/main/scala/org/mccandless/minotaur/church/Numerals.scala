package org.mccandless.minotaur.church

import org.mccandless.minotaur.{Lambda, _}

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
    def apply(n: Term)(implicit r: Reducer): Term = r(Apply(succExpr, n))
  }

  object pred extends ArithmeticExpressions {
    def apply(n: Term)(implicit r: Reducer): Term = r(Apply(predExpr, n))
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
    * The predecessor expression.
    *
    * λn.λf.λx.n (λg.λh.h (g f)) (λu.x) (λu.u)
    */
  val predExpr: Term = Lambda("n", Lambda("f", Lambda("x",
    Apply(
      Apply(
        Apply(
          "n",
          Lambda("g", Lambda("h", Apply("h", Apply("g", "f"))))
        ),
        Lambda("u", "x")
      ),
      Lambda("u", "u")
    )
  )))


  /**
    * The addition expression.
    *
    * Applies f n times, then m more times.
    */
  val addExpr: Term = Lambda(Var("m"), Lambda(Var("n"), Lambda(Var("f"), Lambda(Var("x"),
    Apply(
      Apply(Var("m"), Var("f")),
      Apply(
        Apply(Var("n"), Var("f")),
        Var("x")
      )
    )
  ))))


  /**
    * The subtraction expression for m - n.
    *
    * λm.λn.n PRED m
    */
  val subtractExpr: Term = Lambda("m", Lambda("n", Apply(Apply("n", predExpr), "m")))

  /**
    * The multiplication expression for m * n:
    * λm.λn.λf.m (n f)
    *
    * Repeated addition.
    */
  val multExpr: Term = Lambda("m", Lambda("n", Lambda("f", Apply("m", Apply("n", "f")))))

  val divideExpr: Term = succExpr

  val powExpr: Term = Lambda("b", Lambda("e", Apply("e", "b")))
}


private[church] trait ArithmeticOperators extends ArithmeticExpressions {

  val n: Term

  def +(m: Term)(implicit r: Reducer): Term = r(Apply(Apply(addExpr, n), m))
  def -(m: Term)(implicit r: Reducer): Term = r(Apply(Apply(subtractExpr, n), m))
  def *(m: Term)(implicit r: Reducer): Term = r(Apply(Apply(multExpr, n), m))
  def /(m: Term)(implicit r: Reducer): Term = ???

  def **(m: Term)(implicit r: Reducer): Term = r(Apply(Apply(powExpr, n), m))
}