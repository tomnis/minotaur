package org.mccandless.minotaur

/**
  * A set of typing assumptions.
  *
  * Often written as Γ in the literature.
  *
  * Created by tomas.mccandless on 11/30/17.
  */
case class Context(assumptions: Map[Term, String] = Map.empty) {



  def hasAssumptionFor(term: Term): Boolean = this.assumptions isDefinedAt term


  def getAssumption(term: Term): String = this.assumptions(term)
}
