package com.github.zulkar.transaction

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIBuilder
import org.apache.http.util.EntityUtils
import org.json.JSONObject
import java.math.BigDecimal
import java.net.URI

class HttpClientFixture(private val client: HttpClient, private val serverUri: URI) {
    fun addUser(user: String) {
        val builder = URIBuilder(serverUri).setPath("/users/${user}/create")
        val post = HttpPost(builder.build())
        val response = client.execute(post);
        val jsonObject = JSONObject(EntityUtils.toString(response.entity))
        checkStatus(jsonObject)
    }

    fun replenish(user: String, amount: BigDecimal) {
        val builder = URIBuilder(serverUri).setPath("/operations//replenish/${user}")
                .addParameter("amount", amount.toString())
        val post = HttpPost(builder.build())
        val response = client.execute(post);
        val jsonObject = JSONObject(EntityUtils.toString(response.entity))
        checkStatus(jsonObject)
    }

    fun getBalance(user: String): BigDecimal {
        val builder = URIBuilder(serverUri).setPath("/users/${user}/balance");
        val get = HttpGet(builder.build())
        val response = client.execute(get);
        val jsonObject = JSONObject(EntityUtils.toString(response.entity))
        checkStatus(jsonObject)
        return jsonObject.getBigDecimal("balance")
    }

    fun transer(from: String, to: String, amount: BigDecimal) {
        val builder = URIBuilder(serverUri).setPath("/operations/transfer")
                .addParameter("from", from)
                .addParameter("to", to)
                .addParameter("amount", amount.toString())
        val post = HttpPost(builder.build())
        val response = client.execute(post);
        val jsonObject = JSONObject(EntityUtils.toString(response.entity))
        checkStatus(jsonObject)
    }

    fun getAll(): Map<String, BigDecimal> {
        val builder = URIBuilder(serverUri)
        builder.path = "/users/all";
        val get = HttpGet(builder.build())
        val response = client.execute(get);
        return ObjectMapper().readValue(EntityUtils.toString(response.entity),
                object : TypeReference<Map<String, BigDecimal>>() {})
    }

    private fun checkStatus(jsonObject: JSONObject) {
        val status = jsonObject.getInt("status");
        val message = jsonObject.getString("message");
        if (status != 0) {
            throw RuntimeException(message)
        }
    }

}
