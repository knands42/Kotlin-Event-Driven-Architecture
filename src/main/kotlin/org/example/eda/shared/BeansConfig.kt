package org.example.eda.shared

import org.example.eda.adapter.controller.AccountController
import org.example.eda.adapter.controller.routes
import org.example.eda.adapter.repository.AccountRepositoryImpl
import org.example.eda.adapter.repository.ClientRepositoryImpl
import org.example.eda.adapter.repository.Config
import org.example.eda.domain.event.EventDispatcherInterface
import org.example.eda.domain.event.impl.EventDispatcher
import org.example.eda.domain.repository.AccountRepository
import org.example.eda.domain.repository.ClientRepository
import org.example.eda.usecase.createAccount.CreateAccountUseCase
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

val beans = beans {
    Config.connect()
    Config.migrate()

    bean<ClientRepository> {
        ClientRepositoryImpl()
    }
    bean<AccountRepository> {
        AccountRepositoryImpl(
            ref()
        )
    }
    bean<EventDispatcherInterface> {
        EventDispatcher()
    }
    bean<CreateAccountUseCase> {
        CreateAccountUseCase(
            ref(),
            ref(),
        )
    }
    bean<AccountController> {
        AccountController(
            ref()
        )
    }
    bean(::routes)
}

class BeansConfig : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) =
        beans.initialize(applicationContext)
}