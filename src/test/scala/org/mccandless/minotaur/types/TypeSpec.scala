package org.mccandless.minotaur.types

import org.junit.Test
import org.mccandless.minotaur.{Lambda, Var}
import org.scalatest.Matchers
import org.scalatest.junit.JUnitSuite

/**
  * Created by tomas.mccandless on 11/30/17.
  */
class TypeSpec extends JUnitSuite with Matchers {

  val x = Var("x")
  val y = Var("y")

  val typeOfX = ValType("X")
  val typeOfY = ValType("Y")


  @Test
  def noType(): Unit = {

    val context: Context = Context(Map.empty)

    x.inferType(context) should not be defined
  }


  @Test
  def varType(): Unit = {

    val context: Context = Context(Map(x -> typeOfX))

    x.inferType(context) should be (Some(typeOfX))
  }

  @Test
  def functionType(): Unit = {
    val context: Context = Context(Map(x -> typeOfX, y -> typeOfY))

    Lambda(x, y).inferType(context) should be (Some(FunctionType(typeOfX, typeOfY)))

  }


  @Test
  def functionTypeInContext(): Unit = {
    val id = Lambda(x, x)
    val typeOfId = FunctionType(ValType("A"), ValType("A"))

    val context: Context = Context(Map(id -> typeOfId))

    id.inferType(context) should be (Some(typeOfId))

  }
}
