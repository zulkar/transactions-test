package com.github.zulkar.transaction

import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import java.net.URI
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Tag("integration")
@ExtendWith(ServerTestFixture::class)
class TransactionServerTest {
    lateinit var httpClient: CloseableHttpClient

    @BeforeEach
    fun init() {
        httpClient = HttpClientBuilder
                .create()
                .setMaxConnTotal(1)
                .setMaxConnPerRoute(1)
                .build()
    }

    @AfterEach
    fun close() {
        httpClient.close()
    }


    @Test
    fun testShouldTransfer(@ServerTestFixture.ServerUri uri: URI) {
        val clientFixture = HttpClientFixture(httpClient, uri)
        clientFixture.addUser("Alice")
        clientFixture.addUser("Bob")
        clientFixture.replenish("Alice", 100.toBigDecimal())
        clientFixture.transer("Alice", "Bob", 10.toBigDecimal())
        assertEquals(90.toBigDecimal(), clientFixture.getBalance("Alice"))
        assertEquals(10.toBigDecimal(), clientFixture.getBalance("Bob"))
    }

    @Test
    fun testShouldNotTransferIfNoMoney(@ServerTestFixture.ServerUri uri: URI) {
        val clientFixture = HttpClientFixture(httpClient, uri)
        clientFixture.addUser("Alice")
        clientFixture.addUser("Bob")
        clientFixture.replenish("Alice", 100.toBigDecimal())
        val re = assertThrows<RuntimeException> {
            clientFixture.transer("Alice", "Bob", 120.toBigDecimal())
        }
        assertTrue(re.message!!.startsWith("We Require More Minerals"))
    }

    @Test
    fun testShouldNotTransferIfNegativeMoney(@ServerTestFixture.ServerUri uri: URI) {
        val clientFixture = HttpClientFixture(httpClient, uri)
        clientFixture.addUser("Alice")
        clientFixture.addUser("Bob")
        clientFixture.replenish("Alice", 100.toBigDecimal())
        val re = assertThrows<RuntimeException> {
            clientFixture.transer("Alice", "Bob", (-10).toBigDecimal())
        }
        assertTrue(re.message!!.startsWith("Transfer amount should be positive"))
    }

    @Test
    fun testShouldNotTransferSelf(@ServerTestFixture.ServerUri uri: URI) {
        val clientFixture = HttpClientFixture(httpClient, uri)
        clientFixture.addUser("Alice")
        clientFixture.addUser("Bob")
        clientFixture.replenish("Alice", 100.toBigDecimal())
        val re = assertThrows<RuntimeException> {
            clientFixture.transer("Alice", "Alice", 10.toBigDecimal())
        }
        assertTrue(re.message!!.startsWith("Cannot transfer to self"))
    }

    @Test
    fun testShouldNotTransferZeroMoney(@ServerTestFixture.ServerUri uri: URI) {
        val clientFixture = HttpClientFixture(httpClient, uri)
        clientFixture.addUser("Alice")
        clientFixture.addUser("Bob")
        clientFixture.replenish("Alice", 100.toBigDecimal())
        val re = assertThrows<RuntimeException> {
            clientFixture.transer("Alice", "Bob", 0.toBigDecimal())
        }
        assertTrue(re.message!!.startsWith("Transfer amount should be positive"))
    }

    @Test
    fun testShouldNotTransferMoneyToUnknownUser(@ServerTestFixture.ServerUri uri: URI) {
        val clientFixture = HttpClientFixture(httpClient, uri)
        clientFixture.addUser("Alice")
        clientFixture.addUser("Bob")
        clientFixture.replenish("Alice", 100.toBigDecimal())
        val re = assertThrows<RuntimeException> {
            clientFixture.transer("Alice", "Charlie", 10.toBigDecimal())
        }
        assertTrue(re.message!!.startsWith("User Charlie not exists"))
    }

    @Test
    fun testShouldNotTransferMoneyFromUnknownUser(@ServerTestFixture.ServerUri uri: URI) {
        val clientFixture = HttpClientFixture(httpClient, uri)
        clientFixture.addUser("Alice")
        clientFixture.addUser("Bob")
        clientFixture.replenish("Alice", 100.toBigDecimal())
        val re = assertThrows<RuntimeException> {
            clientFixture.transer("Charlie", "Bob", 10.toBigDecimal())
        }
        assertTrue(re.message!!.startsWith("User Charlie not exists"))
    }

    @Test
    fun testShouldNotCreateUserTwice(@ServerTestFixture.ServerUri uri: URI) {
        val clientFixture = HttpClientFixture(httpClient, uri)
        clientFixture.addUser("Alice")
        val re = assertThrows<RuntimeException> {
            clientFixture.addUser("Alice")
        }
        assertTrue(re.message!!.startsWith("User Alice already exists"))
    }

}
