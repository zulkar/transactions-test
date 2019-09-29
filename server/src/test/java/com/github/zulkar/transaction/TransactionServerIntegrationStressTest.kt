package com.github.zulkar.transaction

import org.apache.commons.lang3.RandomStringUtils
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.net.URI
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference
import kotlin.collections.HashSet
import kotlin.concurrent.thread
import kotlin.test.assertEquals

@Tag("integration")
@ExtendWith(ServerTestFixture::class)
class TransactionServerIntegrationStressTest {

    lateinit var httpClient: CloseableHttpClient

    @BeforeEach
    fun init() {
        httpClient = HttpClientBuilder
                .create()
                .setMaxConnTotal(100)
                .setMaxConnPerRoute(100)
                .build()

    }

    @AfterEach
    fun close() {
        httpClient.close()
    }

    @Test
    fun testCreateUsersTest(@ServerTestFixture.ServerUri uri: URI) {
        val client = HttpClientFixture(httpClient, uri)
        val usernames = generateUsernames(100)
        doTest(100) { client.addUser(usernames[it]) }
        val created = client.getAll()
        assertEquals(usernames.toSet(), created.keys)
    }

    @Test
    fun testCreateUsersAndReplenishBalanceTest(@ServerTestFixture.ServerUri uri: URI) {
        val client = HttpClientFixture(httpClient, uri)
        val usernames = generateUsernames(100)
        val total = AtomicLong()
        doTest(100) {
            client.addUser(usernames[it])
            val amount = Random(it.toLong()).nextInt(1000)
            total.addAndGet(amount.toLong())
            client.replenish(usernames[it], BigDecimal.valueOf(amount.toLong()))
        }

        val created = client.getAll()
        assertEquals(usernames.toSet(), created.keys)
        assertEquals(total.get().toBigDecimal(), created.values.fold(BigDecimal.ZERO) { acc, e -> acc.add(e) })
    }

    @Test
    fun transferMoneyTest(@ServerTestFixture.ServerUri uri: URI) {
        val client = HttpClientFixture(httpClient, uri)
        val usernames = generateUsernames(100)
        val total = AtomicLong()
        doTest(100) {
            client.addUser(usernames[it])
            total.addAndGet(1000)
            client.replenish(usernames[it], 1000.toBigDecimal())
        }

        doTest(100) {
            val rand = Random(it.toLong())
            val from = rand.nextInt(100)
            val to = rand.nextInt(100)
            if (from != to) {
                try {
                    client.transer(usernames[from], usernames[to], rand.nextInt(100).toBigDecimal())
                } catch (e: RuntimeException) {
                    if (!e.message!!.startsWith("We Require More Minerals")) {
                        throw e
                    }
                }

            }

        }

        val created = client.getAll()
        assertEquals(usernames.toSet(), created.keys)
        assertEquals(total.get().toBigDecimal(), created.values.fold(BigDecimal.ZERO) { acc, e -> acc.add(e) })
    }

    fun doTest(count: Int, action: (Int) -> Unit) {
        val startLatch = CountDownLatch(1)
        val stopLatch = CountDownLatch(count)
        val exceptionRef = AtomicReference<Throwable>()
        for (i in 0 until count) {
            thread(start = true) {
                startLatch.await()
                try {
                    action.invoke(i)
                } catch (e: Throwable) {
                    exceptionRef.compareAndSet(null, e)
                } finally {
                    stopLatch.countDown()
                }
            }
        }
        startLatch.countDown();
        stopLatch.await()
        exceptionRef.get()?.let { throw it }

    }


    private fun generateUsernames(count: Int): Array<String> {
        val users = HashSet<String>()
        while (users.size < count) {
            users.add(RandomStringUtils.randomAlphanumeric(10))
        }
        return users.toTypedArray()
    }

}
