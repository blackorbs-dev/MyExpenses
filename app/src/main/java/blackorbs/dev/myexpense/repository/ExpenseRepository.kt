package blackorbs.dev.myexpense.repository

import blackorbs.dev.myexpense.data.ExpenseDao
import blackorbs.dev.myexpense.entities.Expense
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    suspend fun add(expense: Expense){
        expenseDao.add(expense)
    }

    suspend fun get(id: Long) = expenseDao.get(id)

    suspend fun update(expense: Expense){
        expenseDao.update(expense)
    }

    suspend fun delete(expense: Expense){
        expenseDao.delete(expense)
    }

    fun getAllExpenses() = flow {
        emitAll(expenseDao.getAll())
    }
}