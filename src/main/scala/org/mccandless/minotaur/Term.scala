package org.mccandless.minotaur

import org.pmw.tinylog.Logger

/**
  * A term in lambda calculus is one of:
  *   - variable introduction
  *   - function abstraction (lambda)
  *   - function application
  *
  * Created by tomas.mccandless on 7/13/17.
  */
sealed trait Term {

  /** @return the set of free variables in this term. */
  final def freeVars: Set[Var] = this match {
    case v: Var => Set(v)
    case Lambda(arg, body) => body.freeVars - arg
    case Apply(t1, t2) => t1.freeVars union t2.freeVars
  }

  /**
    * Variable substitution.
    *
    * @param old
    * @param newTerm
    * @return
    */
  final def replace(old: Var, newTerm: Term): Term = this match {
    case v: Var if v == old => newTerm
    case Lambda(arg, body) if arg != old && !newTerm.freeVars.contains(arg) => Lambda(arg, body.replace(old, newTerm))
    case Apply(t1, t2) => Apply(t1.replace(old, newTerm), t2.replace(old, newTerm))
    case _ => this
  }

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



