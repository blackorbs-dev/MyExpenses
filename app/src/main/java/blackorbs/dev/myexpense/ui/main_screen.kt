package blackorbs.dev.myexpense.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import blackorbs.dev.myexpense.Page
import blackorbs.dev.myexpense.R
import blackorbs.dev.myexpense.entities.Expense
import blackorbs.dev.myexpense.ui.expense.ExpenseEditLayout
import blackorbs.dev.myexpense.ui.expense.expensePage

@Composable
fun MainScreen(appState: AppState = rememberAppState()){

    var expenseEdit by remember {
        mutableStateOf<Expense?>(null)
    }

    Scaffold(
        topBar = {
            TopBar(
                currentPage = appState.currentPage.value
            )
        },
        floatingActionButton = {
            if(appState.currentPage.value is Page.ExpenseList){
                FAB { expenseEdit = Expense() }
            }
        }
    ) { innerPadding ->

        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = appState.navController,
            startDestination = Page.ExpenseList
        ) {
            expensePage(
                onShowDetails = {
                    appState.navController.navigate(
                        Page.ExpenseDetails(it)
                    )
                },
                onEdit = {
                    expenseEdit = it
                }
            )
        }

        expenseEdit?.let {
            PopupDialog(
                titleRes =
                    if(expenseEdit!!.isNew)
                        R.string.add_expense
                    else R.string.update_expense,
                onDismiss = { expenseEdit = null }
            ) {
                ExpenseEditLayout(
                    expense = expenseEdit!!,
                    onAction = {
                        appState.viewModel.handleAction(
                            it, expenseEdit!!
                        )
                        expenseEdit = null
                    }
                )
            }
        }
    }
}

enum class ActionType{
    Add, Update, Delete
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(currentPage: Page){
    TopAppBar(
        title = {
            Text(
                stringResource(
                    if(currentPage is Page.ExpenseList)
                        R.string.my_expenses
                    else R.string.expense_details
                )
            )
        }
    )
}

@Composable
fun FAB(onClick: () -> Unit){
    FloatingActionButton(onClick =  onClick) {
        Icon(
            Icons.Filled.Add,
            stringResource(R.string.add_expense),
            Modifier.scale(1.3f),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}