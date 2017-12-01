package org.mccandless.minotaur.church

import org.mccandless.minotaur.{Apply, Lambda, Term, Var}

/**
  * Created by ergo on 7/16/17.
  */
object Numerals {

  // interestingly, zero has the same encoding as false
  val zero: Lambda = Booleans.fls
  val one: Lambda = Lambda(Var("x"), Lambda(Var("y"), Apply(Var("x"), Var("y"))))
  val two: Lambda = Lambda(Var("x"), Lambda(Var("y"), Apply(Var("x"), Apply(Var("x"), Var("y")))))
  val three: Lambda = Lambda(Var("x"), Lambda(Var("y"), Apply(Var("x"), Apply(Var("x"), Apply(Var("x"), Var("y"))))))


  val succ: Lambda = Lambda(Var("n"),
    Lambda(Var("f"),
      Lambda(Var("x"),
        Apply(Var("f"),
          Apply( Apply(Var("n"),Var("f")) , Var("x") )
        )
      )
    )
  )


  val +  =    Lambda(Var("m"), Lambda(Var("n"), Lambda(Var("f"), Lambda(Var("x"),
    Apply(
      Apply(Var("m"), Var("f")),
      Apply(
        Apply(Var("n"), Var("f")),
        Var("x")
      )
    )
  ))))

//  val + : Lambda = Lambda(Var("m"), Lambda(Var("n"), Apply(Apply(Var("m"), this.succ), Var("n"))))

  def plus(num1: Term, num2: Term): Term = {
//    Lambda(Var("m"), Lambda(Var("n"), Lambda(Var("f"), Lambda(Var("x"),
//      Apply(
//        Apply(Var("m"), Var("f")),
//        Apply(
//          Apply(Var("n"), Var("f")),
//          Var("x")
//        )
//      )
//    ))))

    Apply(Apply(this.+, num1), num2)
  }
}
