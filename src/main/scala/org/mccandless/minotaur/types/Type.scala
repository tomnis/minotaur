package org.mccandless.minotaur.types

sealed trait Type {

}


case class ValType(name: String) extends Type
case class FunctionType(domain: Type, codomain: Type) extends Type

