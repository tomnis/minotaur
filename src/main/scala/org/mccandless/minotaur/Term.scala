package org.mccandless.minotaur

import org.mccandless.minotaur.types.{Context, FunctionType, Type}
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

  final def vars: Set[Var] = this match {
    case v: Var => Set(v)
    case Lambda(arg, body) => arg.vars union body.vars
    case Apply(t1, t2) => t1.vars union t2.vars
  }

  final def boundVars: Set[Var] = this.vars -- this.freeVars


  /**
    * Variable substitution in a capture-avoiding manner.
    *
    * Abstractions are renamed when their arguments appear free in `newterm`.
    *
    * A new lambda abstraction is created as follows:
    *   - pick a new argument name that is fresh wrt to both the body and `newTerm`.
    *   - pick a new body that is the result of replacing the current arg with the new arg, and `old` with `newTerm`.
    *
    * @param old
    * @param newTerm
    * @return
    */
  final def replace(old: Var, newTerm: Term): Term = this match {
    case v: Var => if (v == old) newTerm else this
    case Lambda(arg, body) if arg != old =>
      if (!newTerm.freeVars.contains(arg))
        Lambda(arg, body.replace(old, newTerm))
      else {
        val newArg: Var = body.vars.union(newTerm.freeVars).max.freshVar
        // TODO this makes multiple traversals, could be optimized
        val newBody: Term = body.replace(arg, newArg).replace(old, newTerm)
        Lambda(newArg, newBody)
      }
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
    * This is useful for alpha-equivalence and capture-avoiding substitution.
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


  /**
    *
    * @param context
    * @return
    */
  final def inferType(context: Context): Option[Type] = {
    Logger.info(s"inferring type of $this")

    // if we have something already in the context, just use that
    if (context.hasAssumptionFor(this)) Option(context.getTypeOf(this))
    // otherwise delegate to the concrete implementation
    else this.specializedInferType(context)
  }


  /**
    *
    * @param context
    * @return
    */
  protected[this] def specializedInferType(context: Context): Option[Type]
}






/**
  * Variable introduction.
  *
  * @param name
  */
case class Var(name: String) extends Term with Ordered[Var] {

  override def compare(that: Var): Int = this.name compareTo that.name

  // we dont need to do anything specific here
  override protected[this] def specializedInferType(context: Context): Option[Type] = None
}

/**
  * Abstraction of a variable from a term
  *
  * @param arg
  * @param body
  */
// An occurrence of the variable arg is said to be bound when it occurs in the body body of an abstraction λ arg.body
// An occurrence of arg is free if it appears in a position where it is not bound by an enclosing abstraction on arg
// TODO consider adding an alternate constructor to represent the short syntax form of construction, id \xyz...
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


  override protected[this] def specializedInferType(context: Context): Option[Type] = {
    for {
      argType <- context.maybeTypeOf(this.arg)
      bodyType <- context.maybeTypeOf(this.body)
    } yield FunctionType(argType, bodyType)
  }
}

object Lambda {
  def apply(arg: String, body: String): Lambda = Lambda(Var(arg), Var(body))
  def apply(arg: String, body: Term): Lambda = Lambda(Var(arg), body)
}

/**
  * Term application
  *
  * @param t1
  * @param t2
  */
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

  override protected[this] def specializedInferType(context: Context): Option[Type] = {
    for {
      t2Type: Type <- t2.inferType(context)
      t1Type: Type <- t1.inferType(context) if t1Type.isInstanceOf[FunctionType] && t1Type.asInstanceOf[FunctionType].domain == t2Type
    } yield {
      t1Type.asInstanceOf[FunctionType].codomain
    }
  }
}

object Apply {
  def apply(t1: String, t2: String): Apply = Apply(Var(t1), Var(t2))
  def apply(t1: String, t2: Term): Apply = Apply(Var(t1), t2)
  def apply(t1: Term, t2: String): Apply = Apply(t1, Var(t2))
}
