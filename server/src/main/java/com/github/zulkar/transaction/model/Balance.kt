package com.github.zulkar.transaction.model

import java.math.BigDecimal
import java.util.concurrent.atomic.AtomicReference

class Balance(initial: BigDecimal) {
    public constructor() : this(BigDecimal.valueOf(0))

    private val ref = AtomicReference<BigDecimal>(initial)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    fun add(value: BigDecimal): BigDecimal {
        var previous: BigDecimal
        var newValue: BigDecimal
        do {
            previous = ref.get();
            newValue = previous.add(value)
        } while (!ref.compareAndSet(previous, newValue))
        return newValue
    }

    fun withdraw(value: BigDecimal) = add(value.negate())


    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}