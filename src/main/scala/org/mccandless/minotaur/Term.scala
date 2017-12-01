package org.mccandless.minotaur

import org.pmw.tinylog.Logger

import scala.util.{Failure, Success, Try}

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
  // TODO make tailrec
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
    case Lambda(arg, body) if arg != old =>
      if (!newTerm.freeVars.contains(arg)) Lambda(arg, body.replace(old, newTerm))
        // TODO likely bug here. do we ever need to make the arg itself fresh?
      else Lambda(arg, newTerm.replace(arg, newTerm.freshVar))
    case Apply(t1, t2) => Apply(t1.replace(old, newTerm), t2.replace(old, newTerm))
    case _ => this
  }

  /**
    * A Term with no free variables is said to be closed (ie, a combinator)
    *
    * @return true iff there are no free variables in this term.
    */
  final def isClosed: Boolean = this.freeVars.isEmpty


  /** @return a fresh variable for this term. */
  // TODO more robust increments, this just appends a '
  final def freshVar: Var = this match {
    case Var(name) => Var(s"$name'")
    case Lambda(arg, body) => body.freeVars.max.freshVar
    case Apply(t1, t2) => t1.freeVars.union(t2.freeVars).max.freshVar
  }

  override def toString: String = this match {
    case Var(name) => name
    case Lambda(arg, body) => s"λ$arg.$body"
    case Apply(t1, t2) => s"($t1 $t2)"
  }


  /**
    * Converts to a [[DeBruijnTerm]] with De Bruijn indices as identifiers instead of raw strings.
    *
    * @return
    */
  final def toDeBruijn: DeBruijnTerm = {

    def deBruijnHelper(env: List[String], term: Term): DeBruijnTerm = {
      Logger.debug(s"converting $term to debruijn index, env = $env")
      term match {
        case v: Var => DeBruijnVar(env.indexOf(v.name))
        case l: Lambda => DeBruijnLambda(deBruijnHelper(l.arg.name :: env, l.body))
        case a: Apply => DeBruijnApply(deBruijnHelper(env, a.t1), deBruijnHelper(env, a.t2))
      }
    }

    deBruijnHelper(List.empty, this)
  }

  /**
    * Checks for equivalence up to variable renaming.
    *
    * @param that
    * @return
    */
  final def isAlphaEquivalentTo(that: Term): Boolean = {
    this.toDeBruijn == that.toDeBruijn
  }


  /** @return true iff we are in a normal form (cannot be further reduced). */
  final def isNormal: Boolean = this.reduce == this

  /**
    * Constructs a new reduced form of this [[Term]].
    *
    * TODO reduction order strategies.
    *
    * @return
    */
  final def reduce: Term = {
    Logger.info(s"reducing $this")
    val tPrime = this match {
      case v: Var => v
      case Lambda(arg, body) => Lambda(arg, body.reduce)
      case Apply(t1: Lambda, t2: Term) => Apply(t1, t2.reduce).beta
      case Apply(t1, t2) => Apply(t1.reduce, t2.reduce)
    }

    if (this.isAlphaEquivalentTo(tPrime)) tPrime
    else tPrime.reduce
  }




  final def inferType(context: Context): String = {
    Logger.info(s"inferring type of $this")

//    if (context.hasAssumptionFor(this)) context.getAssumption(this)
//    else

    ""
  }
}






/**
  * Variable introduction.
  *
  * @param name
  */
case class Var(name: String) extends Term with Ordered[Var] {

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
    val nextFreeVar: Var = this.freshVar
    require(!body.freeVars.contains(nextFreeVar))
    Lambda(nextFreeVar, this.body.replace(this.arg, nextFreeVar))
  }


  /**
    * An eta-conversion
    * @return
    */
  def eta: Try[Term] = {
    this.body match {
      case Apply(t1, t2) if t2 == this.arg && !t1.freeVars.contains(this.arg) => Success(t1)
      case _ => Failure(new RuntimeException(s"eta reduction not possible for $this"))
    }
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
