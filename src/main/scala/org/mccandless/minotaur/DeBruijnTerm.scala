package org.mccandless.minotaur

/**
  * Similar to [[Term]], except De Bruijn indices are used instead of raw string names.
  *
  * Each De Bruijn index is a natural number that represents an occurrence of a variable in a λ-term,
  * and denotes the number of binders that are in scope between that occurrence and its corresponding binder.
  *
  * TODO this isn't too useful as we can really only use it for alpha equivalence. Maybe we should define reduction here as well
  * that suggests the need for a supertrait of both [[Term]] and [[DeBruijnTerm]] where common logic can be defined.
  */
sealed trait DeBruijnTerm {

  /**
    * Terms encoded with De Bruijn indices can be directly compared at the syntactic level.
    *
    * Since the special index encoding is used rather than raw string identifiers, we won't lose equivalence
    * w.r.t. choice of bound variable names.
    *
    * @param that
    * @return
    */
  def isAlphaEquivalentTo(that: DeBruijnTerm): Boolean = this == that
}

case class DeBruijnVar(index: Int) extends DeBruijnTerm

case class DeBruijnLambda(expr: DeBruijnTerm) extends DeBruijnTerm

case class DeBruijnApply(t1: DeBruijnTerm, t2: DeBruijnTerm) extends DeBruijnTerm

