package org.mccandless.minotaur.types

/**
  * A type in the lambda calculus.
  */
sealed trait Type {

}


case class ValType(name: String) extends Type
case class FunctionType(domain: Type, codomain: Type) extends Type {

  override def toString: String = s"(${this.domain} => ${this.codomain})"
}

