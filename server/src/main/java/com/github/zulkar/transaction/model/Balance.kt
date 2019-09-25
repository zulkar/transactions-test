package com.github.zulkar.transaction.model

import java.math.BigDecimal
import java.util.concurrent.atomic.AtomicReference

class Balance(initial: BigDecimal) {
    public constructor() : this(BigDecimal.valueOf(0))

    private val ref = AtomicReference<BigDecimal>(initial)

    fun get(): BigDecimal = ref.get()

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

    fun tryBlock(value: BigDecimal): Boolean {
        var previous: BigDecimal
        var newValue: BigDecimal
        do {
            previous = ref.get();
            newValue = previous.subtract(value)
            if (newValue.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
        } while (!ref.compareAndSet(previous, newValue))
        return true
    }


}