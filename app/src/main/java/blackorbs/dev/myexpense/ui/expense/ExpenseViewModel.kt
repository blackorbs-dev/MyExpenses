package blackorbs.dev.myexpense.ui.expense

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import blackorbs.dev.myexpense.MainApp
import blackorbs.dev.myexpense.entities.Expense
import blackorbs.dev.myexpense.repository.ExpenseRepository
import blackorbs.dev.myexpense.ui.ActionType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val repository: ExpenseRepository
): ViewModel() {

    private val _expense = mutableStateOf<Expense?>(null)
    val expense: State<Expense?> = derivedStateOf { _expense.value }

    val allExpenses = repository.getAllExpenses().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    fun handleAction(actionType: ActionType, expense: Expense){
        when(actionType){
            ActionType.Add -> add(expense)
            ActionType.Update -> update(expense)
            ActionType.Delete -> delete(expense)
        }
    }

    private fun add(expense: Expense){
        viewModelScope.launch {
            repository.add(expense.apply {
                isNew = false
            })
        }
    }

    fun get(id: Long){
        viewModelScope.launch {
            _expense.value = repository.get(id)
        }
    }

    private fun update(expense: Expense){
        viewModelScope.launch {
            repository.update(expense)
        }
    }

    private fun delete(expense: Expense){
        viewModelScope.launch {
            repository.delete(expense)
        }
    }

    companion object{
        val Factory = viewModelFactory {
            initializer {
                ExpenseViewModel(
                    ExpenseRepository(
                        (this[APPLICATION_KEY] as MainApp)
                            .appModule.expenseDao
                    )
                )
            }
        }
    }
}