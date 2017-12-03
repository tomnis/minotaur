import org.mccandless.minotaur._
import org.mccandless.minotaur.types.{Context, ValType}

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