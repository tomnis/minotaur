package org.mccandless.minotaur.church

import org.mccandless.minotaur.{Apply, Lambda, Term, Var}

/**
  * Church encoding of lists implemented using pairs.
  *
  * Created by ergo on 7/16/17.
  */
object Lists {
  import Pairs._
  import Booleans._

  // the empty list
  val nil: Term = Apply(Apply(pair, tru), tru)

  val isNil: Lambda = first

  val cons: Lambda = Lambda(Var("h"), Lambda(Var("t"), Apply(
      Apply(pair, fls),
      Apply(
        Apply(pair, Var("h")),
        Var("x")
      )
    )))

  val head: Lambda = Lambda(Var("z"), Apply(first, Apply(second, Var("z"))))
  val tail: Lambda = Lambda(Var("z"), Apply(second, Apply(second, Var("z"))))
}
