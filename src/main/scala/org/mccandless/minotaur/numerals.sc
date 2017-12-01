import org.mccandless.minotaur._
import org.mccandless.minotaur.church.Numerals

val b = Numerals.one

val c = Apply(Numerals.succ, Numerals.zero)

val d = c.reduce

b.isAlphaEquivalentTo(d)

