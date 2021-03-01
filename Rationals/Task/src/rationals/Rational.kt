package rationals

import java.math.BigInteger

data class Rational(val n: BigInteger, val d: BigInteger) : Comparable<Rational> {
    private val gdc: BigInteger = n.gcd(d)
    val isPositive: Boolean
    val normalizedNumerator = n / gdc
    val normalizedDenominator = d / gdc
    val numeratorSigvalue: Int = normalizedNumerator.signum()
    val denominatorSigvalue: Int = normalizedDenominator.signum()

    init {
        if (d.equals(0)) throw IllegalArgumentException()

        isPositive = when {
            numeratorSigvalue == 1 && denominatorSigvalue == 1 || numeratorSigvalue == -1 && denominatorSigvalue == -1 -> true
            else -> false
        }
    }

    companion object {
        fun normalize(n: BigInteger, d: BigInteger): Rational {
            val gcd = n.gcd(d)

            return Rational(n / gcd, d / gcd)

        }
    }

    override operator fun compareTo(other: Rational): Int {
        val firstNumerator = normalizedNumerator * other.normalizedDenominator
        val secondNumerator = other.normalizedNumerator * normalizedDenominator
        return firstNumerator.compareTo(secondNumerator)
    }

    operator fun rangeTo(end: Rational): ClosedRange<Rational> {
        return object : ClosedRange<Rational> {
            override val endInclusive: Rational = end
            override val start: Rational = this@Rational
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Rational

        if (isPositive == other.isPositive && normalizedNumerator.multiply(numeratorSigvalue.toBigInteger()) == other.normalizedNumerator.multiply(
                other.numeratorSigvalue.toBigInteger()
            ) && normalizedDenominator.multiply(denominatorSigvalue.toBigInteger()) == other.normalizedDenominator.multiply(
                other.denominatorSigvalue.toBigInteger()
            )
        ) return true

        return false
    }

    override fun hashCode(): Int {
        var result = 17
        result = 31 * result + normalizedNumerator.multiply(numeratorSigvalue.toBigInteger()).hashCode()
        result = 31 * result + normalizedDenominator.multiply(denominatorSigvalue.toBigInteger()).hashCode()
        return result
    }

    override fun toString(): String {
        val denominatorSigvalue: Int = normalizedDenominator.signum()
        return when {
            BigInteger.ONE == normalizedDenominator -> "$normalizedNumerator"
            denominatorSigvalue == 1 -> "$normalizedNumerator/$normalizedDenominator"
            denominatorSigvalue == -1 -> "${-normalizedNumerator}/${-normalizedDenominator}"
            else -> throw IllegalArgumentException()
        }
        return if (BigInteger.ONE == normalizedDenominator) "$normalizedNumerator" else "$normalizedNumerator/$normalizedDenominator"
    }
}

operator fun Rational.plus(other: Rational): Rational {
    return Rational(n * other.d + other.n * d, d * other.d)
}

operator fun Rational.minus(other: Rational): Rational {
    return Rational(n * other.d - other.n * d, d * other.d)
}

operator fun Rational.times(other: Rational): Rational {
    return Rational(n * other.n, d * other.d)
}

operator fun Rational.div(other: Rational): Rational {
    return Rational(n * other.d, d * other.n)
}

operator fun Rational.unaryMinus(): Rational {
    return Rational(-n, d)
}

fun String.toRational(): Rational {
    val rational = this.split('/')
    return Rational(rational[0].toBigInteger(), if (rational.size == 2) rational[1].toBigInteger() else BigInteger("1"))
}

infix fun Int.divBy(i: Int): Rational = Rational(this.toBigInteger(), i.toBigInteger())
infix fun Long.divBy(i: Long): Rational = Rational(this.toBigInteger(), i.toBigInteger())
infix fun BigInteger.divBy(i: BigInteger): Rational = Rational(this, i)

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println(
        "912016490186296920119201192141970416029".toBigInteger() divBy
                "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2
    )
}