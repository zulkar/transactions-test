package com.github.zulkar.transaction.errors

import com.github.zulkar.transaction.model.User
import java.math.BigDecimal

object BusinessErrorUtil {

    @JvmStatic
    fun throwAlreadyExistsException(user: User) {
        throw BusinessException(1, "User ${user.name} already exists")
    }

    @JvmStatic
    fun throwUserNotExists(user: User?) {
        throw BusinessException(2, "User ${user?.name ?: ""} not exists")
    }

    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    fun throwTransferAmountShouldBePositive(amount: BigDecimal?) {
        throw BusinessException(3, "Transfer amount should be positive")

    }

    @JvmStatic
    fun throwNotEnoughMoney(user: User, amount: BigDecimal) {
        throw BusinessException(4, "We Require More Minerals. User ${user.name} does not have ${amount}")
    }
}
