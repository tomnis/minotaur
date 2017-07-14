package org.mccandless.minotaur

import org.pmw.tinylog.Logger

/**
  * Created by tomas.mccandless on 7/13/17.
  */
sealed trait Term {

  /** @return the set of free variables in this term. */
  def freeVars: Set[Var]

  def replace(old: Var, newTerm: Term): Term

  /**
    * A Term with no free variables is said to be closed (ie, a combinator)
    *
    * @return true iff there are no free variables in this term.
    */
  final def isClosed: Boolean = this.freeVars.isEmpty
}



/**
  * Variable introduction.
  *
  * @param name
  */
case class Var(name: String) extends Term with Ordered[Var] {

  override def freeVars: Set[Var] = Set(this)

  override def replace(old: Var, newTerm: Term): Term = {
    if (this == old) newTerm
    else this
  }

  /** @return a fresh variable */
  // TODO more robust increments, this just appends a '
  def next: Var = Var(s"$name'")

  override def compare(that: Var): Int = this.name compareTo that.name
}

/**
  * Abstraction of a variable from a term
  *
  * @param arg
  * @param body
  */
// An occurrence of the variable arg is said to be bound when it occurs in the body body of an abstraction λ arg.body
// An occurrence of arg is free if it appears in a position where it is not bound by an enclosing abstraction on arg
case class Lambda(arg: Var, body: Term) extends Term {

  override def freeVars: Set[Var] = this.body.freeVars - arg

  override def replace(old: Var, newTerm: Term): Lambda = {
    if (this.arg != old && !newTerm.freeVars.contains(this.arg)) Lambda(this.arg, this.body.replace(old, newTerm))
    else this
  }

  /**
    * Variable renaming to avoid capture.
    *
    * @return
    */
  // TODO should we move this to the base trait?
  def alpha: Lambda = {
    val nextFreeVar: Var = this.body.freeVars.max.next

    if (body.freeVars contains nextFreeVar) {
      throw new RuntimeException(s"we expect $nextFreeVar to be free in $body")
    }

    Lambda(nextFreeVar, this.body.replace(this.arg, nextFreeVar))
  }
}


/**
  * Term application
  *
  * @param t1
  * @param t2
  */
// TODO should t1 be a lambda?
case class Apply(t1: Term, t2: Term) extends Term {

  override def freeVars: Set[Var] = t1.freeVars union t2.freeVars

  override def replace(old: Var, newTerm: Term): Term = Apply(t1.replace(old, newTerm), t2.replace(old, newTerm))

  /**
    * Beta reduction. A single step of evaluation.
    *
    * @return
    */
  def beta: Term = this.t1 match {
    case Lambda(arg, body) =>
      body.replace(arg, this.t2)
    // TODO is this right? is it ever possible that we would actually call beta in this situation?
    case _ =>
      Logger.error(s"we shouldn't be calling beta on $this")
      this
  }
}



