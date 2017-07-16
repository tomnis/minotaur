package org.mccandless.minotaur.church

import org.mccandless.minotaur.{Apply, Lambda, Var}

/**
  * Created by ergo on 7/16/17.
  */
object Pairs {
  import Booleans._

  val pair: Lambda = Lambda(Var("x"), Lambda(Var("y"), Lambda(Var("z"), Apply(Apply(Var("z"), Var("x")), Var("y")))))

  val first: Lambda = Lambda(Var("p"), Apply(Var("p"), tru))
  val second: Lambda = Lambda(Var("p"), Apply(Var("p"), fls))
}
