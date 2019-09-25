package com.github.zulkar.transaction.model

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class BalanceTest {

    @Test
    @Tag("multithread")
    fun testMultithreadedAddBalance() {
        val threadCount = 1000
        val balance = Balance(BigDecimal.valueOf(1000));

        val startLatch = CountDownLatch(1);
        val stopLatch = CountDownLatch(threadCount);
        for (i in 1..threadCount) {
            thread(start = true) {
                startLatch.await()
                balance.add(BigDecimal.valueOf(1))
                stopLatch.countDown()
            }
        }
        startLatch.countDown()
        stopLatch.await()
        assertEquals(1000 + threadCount, balance.get().intValueExact())
    }

    @Test
    @Tag("multithread")
    fun testMultithreadedBlockBalance() {
        val threadCount = 1000
        val balance = Balance(BigDecimal.valueOf(1000));

        val startLatch = CountDownLatch(1);
        val stopLatch = CountDownLatch(threadCount);
        val counter = AtomicInteger()
        for (i in 1..threadCount) {
            thread(start = true) {
                startLatch.await()
                if (balance.tryBlock(BigDecimal.valueOf(70))) {
                    counter.incrementAndGet();
                }
                stopLatch.countDown()
            }
        }

        startLatch.countDown()
        stopLatch.await()
        assertEquals(1000 / 70, counter.get());
        assertEquals(1000 % 70, balance.get().intValueExact());
    }

    @Test
    fun shouldBlockMoney() {
        val balance = Balance(BigDecimal.valueOf(1000));
        assertTrue(balance.tryBlock(BigDecimal.valueOf(200)))
        assertEquals(800, balance.get().intValueExact())
    }

    @Test
    fun shouldAddMoney() {
        val balance = Balance(BigDecimal.valueOf(1000));
        balance.add(BigDecimal.valueOf(100))
        assertEquals(1100, balance.get().intValueExact())
    }

    @Test
    fun shouldNotBlockMoneyIfNotEnough() {
        val balance = Balance(BigDecimal.valueOf(100));
        assertFalse(balance.tryBlock(BigDecimal.valueOf(200)))
        assertEquals(100, balance.get().intValueExact())
    }
}