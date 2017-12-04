package org.mccandless.minotaur

import org.mccandless.minotaur.utils.Memoizer
import org.pmw.tinylog.Logger

/**
  * Created by tomas.mccandless on 7/13/17.
  */
trait Reducer {

  /**
    * Reduces `expr` into its base form.
    *
    * @param expr
    * @return
    */
  def apply(expr: Term): Term

  /**
    * We can say that a term is normal if it cannot be further reduced.
    *
    * @param expr
    * @return
    */
  def isNormal(expr: Term) = expr.isAlphaEquivalentTo(this.apply(expr))
}


/**
  * Under full beta-reduction strategy, any reducible expression may be reduced at any time.
  */
trait FullBetaReducer extends Reducer {

//  override def reduce(expr: Term) = ???
}

/**
  * Under normal-order strategy, the leftmost, outermost reducible expression is always reduced first.
  */
trait NormalOrderReducer extends Reducer {

//  override def reduce(expr: Term) = ???
}


/**
  * Under call-by-name strategy, no reductions are allowed inside abstractions.
  *
  * Haskell uses a similar strategy, call-by-need
  */
trait CallByNameReducer extends Reducer {
//  override def reduce(expr: Term): Term = ???
}

/**
  * Under call-by-value strategy, only outermost reducible expressions are reduced, and a reducible expression is reduced
  * only when its right-hand side has already been reduced to a value.
  */
object CallByValue extends Reducer {

  private[this] val memoizer: Memoizer[Term, Term] = new Memoizer
  private[this] val memoizedReduce: Term => Term = this.memoizer.memoize(this.reduce)

  def reduce(expr: Term): Term = {
    Logger.info(s"reducing $expr")

    val exprPrime: Term = expr match {
      case v: Var => v
      case Lambda(arg, body) => Lambda(arg, this(body))
      case Apply(t1: Lambda, t2: Term) => Apply(t1, this(t2)).beta
      case Apply(t1, t2) => Apply(this(t1), this(t2))
    }

    if (expr isAlphaEquivalentTo exprPrime) exprPrime else this(exprPrime)
  }


  /**
    * We implement a memoized form of evaluation for efficiency.
    *
    * @param expr
    * @return
    */
  override def apply(expr: Term) = this.memoizedReduce(expr)
}
