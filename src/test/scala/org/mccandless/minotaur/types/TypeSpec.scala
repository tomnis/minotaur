package org.mccandless.minotaur.types

import org.junit.Test
import org.mccandless.minotaur.{Apply, BaseSpec, Lambda, Var}

/**
  * Created by tomas.mccandless on 11/30/17.
  */
class TypeSpec extends BaseSpec {

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
  def applicationType(): Unit = {

    // create some variables and their types
    val x = Var("x")
    val y = Var("y")
    val z = Var("z")
    val intType = ValType("Int")
    val boolType = ValType("Bool")

    val context = Context(Map(x -> intType, y -> boolType, z -> intType))

    // we expect that the type should be Int => Bool
    val inferredFunctionType = Lambda(x, y).inferType(context)

    // we expect that the type should be Bool
    val inferredApplicationType = Apply(Lambda(x, y), z).inferType(context)
    inferredApplicationType should be (Some(boolType))
  }


  @Test
  def functionTypeInContext(): Unit = {
    val id = Lambda(x, x)
    val typeOfId = FunctionType(ValType("A"), ValType("A"))

    val context: Context = Context(Map(id -> typeOfId))

    id.inferType(context) should be (Some(typeOfId))

  }
}
