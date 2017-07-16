package org.mccandless.minotaur

import Combinators._
import org.junit.{Ignore, Test}
import org.pmw.tinylog.Logger
import org.scalatest.Matchers
import org.scalatest.junit.JUnitSuite

/**
  * Created by tomas.mccandless on 7/13/17.
  */
class TermSpec extends JUnitSuite with Matchers {

  val x = Var("x")
  val y = Var("y")

  @Test
  def varEquality(): Unit = {
    x should be (x)
    x should not be y
  }


  @Test
  def termCreation(): Unit = {
    val id: Term = Lambda(Var("arg"), x)
    val app: Term = Apply(id, id)
  }


  @Test
  def freeVars(): Unit = {
    val id: Term = Lambda(Var("arg"), Var("arg"))
    id.freeVars should be (empty)

    val xy: Term = Lambda(Var("y"), Apply(Var("arg"), Var("y")))
    xy.freeVars should be (Set(Var("arg")))
  }


  /**
    * The second occurrence of arg is free. We'll ignore this for now, and just assume all variables are fresh.
    */
  @Test
  @Ignore
  def scope(): Unit = {
    Apply(Lambda(Var("arg"), Var("arg")), Var("arg")).freeVars should be (Set(Var("arg")))
  }


  @Test
  def alpha(): Unit = {
    id.alpha should be (Lambda(Var("x'"), Var("x'")))
  }


  /** Checks that we can properly apply beta reductions. */
  @Test
  def beta(): Unit = {
    Apply(id, Var("y")).beta should be (Var("y"))
    Apply(id, id).beta should be (id)
  }


  @Test
  def substitute(): Unit = {
    val x = Var("x")
    val y = Var("y")
    x.replace(x, id) should be (id)
    // trying to replace for a different variable should have no effect
    x.replace(y, id) should be (x)

    // check lambda replacing
    id.replace(x, id) should be (id)
    id.replace(x, y) should be (id)
  }


  @Test
  def termToString(): Unit = {
    id.toString should be ("λx.x")
  }
}
