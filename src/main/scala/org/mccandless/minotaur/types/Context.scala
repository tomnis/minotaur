package org.mccandless.minotaur.types

import org.mccandless.minotaur.Term

/**
  * A set of typing assumptions.
  *
  * Often written as Γ in the literature.
  *
  * Created by tomas.mccandless on 11/30/17.
  */
case class Context(assumptions: Map[Term, Type] = Map.empty) {

  /**
    * Whether we have a type assumption defined for `term`.
    *
    * @param term
    * @return
    */
  def hasAssumptionFor(term: Term): Boolean = this.assumptions isDefinedAt term

  @throws[RuntimeException]("when there is no such assumption")
  def getTypeOf(term: Term): Type = this.assumptions(term)


  def maybeTypeOf(term: Term): Option[Type] = this.assumptions get term
}
