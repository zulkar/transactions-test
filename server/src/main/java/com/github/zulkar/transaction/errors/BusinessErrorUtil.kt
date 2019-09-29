package com.github.zulkar.transaction.errors

import com.github.zulkar.transaction.model.User
import org.apache.logging.log4j.LogManager
import java.math.BigDecimal

object BusinessErrorUtil {

    val logger = LogManager.getLogger(BusinessErrorUtil.javaClass)
    @JvmStatic
    fun throwAlreadyExistsException(user: User) {
        logger.info("User {} already exists", user)
        throw BusinessException(1, "User ${user.name} already exists")
    }

    @JvmStatic
    fun throwUserNotExists(user: User?) {
        logger.info("User {} not exists", user)
        throw BusinessException(2, "User ${user?.name ?: ""} not exists")
    }

    @JvmStatic
    fun throwTransferAmountShouldBePositive(amount: BigDecimal?) {
        logger.info("amount should be positive but was {}", amount)
        throw BusinessException(3, "Transfer amount should be positive")

    }

    @JvmStatic
    fun throwNotEnoughMoney(user: User, amount: BigDecimal) {
        logger.info("User {} has not enough money to perform operation with amount {}", user, amount)
        throw BusinessException(4, "We Require More Minerals. User ${user.name} does not have ${amount}")
    }

    @JvmStatic
    fun throwCannotTransferSelf(user: User) {
        logger.info("User {} trying to transfer to self", user)
        throw BusinessException(5, "Cannot transfer to self")
    }

}
