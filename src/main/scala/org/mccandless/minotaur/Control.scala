package org.mccandless.minotaur

/**
  *
  * Created by tdm on 12/3/17.
  */
object Control {

  object fix extends ControlExpressions {

    def apply(t: Term)(implicit r: Reducer): Term = r(Apply(yExpr, t))
  }
}


trait ControlExpressions {

  /**
    *
    * Y := λg.(λx.g (x x)) (λx.g (x x))
    */
  val yExpr: Term = Lambda("g", Apply(
    Lambda("x", Apply("g", Apply("x", "x"))),
    Lambda("x", Apply("g", Apply("x", "x")))
  ))
}