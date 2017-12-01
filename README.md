# minotaur
An experimental implementation of typed lambda calculus

## lambda calculus

Lambda calculus is a simple yet universal model of computation defined by Church in the 30s.
In terms of computational power, lambda calculus and Turing machines are equivalent.
In other words, any computation can be represented in either model.

There is a beauty in the sparse simplicity of the syntax. Every term in the language is either
- a variable: x
- a function definition (lambda abstraction): (λx.M)
- a function application: (M N)

Everything is a function. 

Function calls are evaluated by term-rewriting and symbol substitution.
Suppose `f` is a function with a single parameter `x`. When we apply `f` to an argument `y`,
`y` is substituted for every bound occurrence of `x` in `f` to produce a new term.

For example, `(λx.x) (y)` evaluates to `y`.

It is quite interesting that all our familiar datatypes such as `Int`, `Bool`, `Tuple`, `List` can be
represented using only functions.

Modern functional languages can be implemented as syntactic sugar on top of the calculus.
(Haskell, ML)


### α-equivalence

This form of equivalence captures the notion that choice of bound variables should not matter.
For example, `(λx.x)` and `(λy.y)` both denote the identity function. 


### Beta-reduction



### Church numerals

A Church numeral is a way to encode the natural numbers:

```
    0 := λf.λx.x
    1 := λf.λx.f x
    2 := λf.λx.f (f x)
    3 := λf.λx.f (f (f x))
```

A number `n` is represented as a higher order function that accepts a function `f` and applies
`f` `n` times.
