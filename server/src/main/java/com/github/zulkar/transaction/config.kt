package com.github.zulkar.transaction

import com.github.zulkar.transaction.processing.ProcessingService
import com.github.zulkar.transaction.processing.ProcessingServiceImpl
import com.github.zulkar.transaction.web.BusinessExceptionMapper
import com.github.zulkar.transaction.web.TransferService
import com.github.zulkar.transaction.web.UserService
import org.glassfish.jersey.internal.inject.AbstractBinder
import org.glassfish.jersey.server.ResourceConfig




class MyApplicationBinder : AbstractBinder() {
    override fun configure() {
        bind(ProcessingServiceImpl::class.java).to(ProcessingService::class.java)
        bind(UserService::class.java).to(UserService::class.java)
        bind(TransferService::class.java).to(TransferService::class.java)
    }
}

class MyApplication : ResourceConfig() {
    init {
        packages("com.github.zulkar.transaction.web")
        register(MyApplicationBinder())
        register(BusinessExceptionMapper())
    }
}