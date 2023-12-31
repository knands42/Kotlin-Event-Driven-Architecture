package org.example.eda.domain.repositories

import org.example.eda.adapter.repository.AccountRepositoryImpl
import org.example.eda.adapter.repository.ClientRepositoryImpl
import org.example.eda.adapter.repository.Config
import org.example.eda.adapter.repository.TransactionRepositoryImpl
import org.example.eda.domain.entity.Account
import org.example.eda.domain.entity.Client
import org.example.eda.domain.entity.Transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

internal class TransactionDbTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            Config.connect()
            Config.migrate()
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            Config.migrateDown()
        }
    }

    @Test
    fun `should save a transaction`() {
        val clientRepositoryImpl = ClientRepositoryImpl()
        val accountRepositoryImpl = AccountRepositoryImpl(clientRepositoryImpl)
        val transactionRepositoryImpl = TransactionRepositoryImpl(accountRepositoryImpl)
        val client = Client(
            name = "John Doe",
            email = "mail@mail.com",
        )
        val account = Account(
            balance = 100.0f,
            client = client,
        )
        val transaction = Transaction(
            accountFrom = account,
            accountTo = account,
            amount = 100.0f,
        )
        clientRepositoryImpl.create(client)
        accountRepositoryImpl.create(account)
        transactionRepositoryImpl.create(transaction)

        val transactionResult = transactionRepositoryImpl.getById(transaction.id)
        assert(transactionResult!!.accountFrom.id == transaction.accountFrom.id)
        assert(transactionResult.accountTo.id == transaction.accountTo.id)
        assert(transactionResult.amount == transaction.amount)
    }
}