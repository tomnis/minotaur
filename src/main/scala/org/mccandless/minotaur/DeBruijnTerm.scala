package org.mccandless.minotaur

/** Similar to [[Term]], except De Bruijn indices are used instead of raw string names. */
sealed trait DeBruijnTerm

case class DeBruijnVar(index: Int) extends DeBruijnTerm

case class DeBruijnLambda(expr: DeBruijnTerm) extends DeBruijnTerm

case class DeBruijnApply(t1: DeBruijnTerm, t2: DeBruijnTerm) extends DeBruijnTerm

