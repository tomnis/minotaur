package org.mccandless.minotaur

/**
  * Created by tomas.mccandless on 7/13/17.
  */
trait Reducer {

  def reduce(application: Apply): Term

}


/**
  * Under full beta-reduction strategy, any reducible expression may be reduced at any time.
  */
object FullBetaReducer extends Reducer {

  override def reduce(application: Apply) = ???
}

/**
  * Under normal-order strategy, the leftmost, outermost reducible expression is always reduced first.
  */
object NormalOrderReducer extends Reducer {

  override def reduce(application: Apply) = ???
}


/**
  * Under call-by-name strategy, no reductions are allowed inside abstractions.
  *
  * Haskell uses a similar strategy, call-by-need
  */
object CallByNameReducer extends Reducer {
  override def reduce(application: Apply): Term = ???
}

/**
  * Under call-by-value strategy, only outermost reducible expressions are reduced, and a reducible expression is reduced
  * only when its right-hand side has already been reduced to a value.
  */
object CallByValueReducer extends Reducer {

  override def reduce(application: Apply): Term = ???
}
