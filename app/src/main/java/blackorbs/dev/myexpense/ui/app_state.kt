package blackorbs.dev.myexpense.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import blackorbs.dev.myexpense.Page
import blackorbs.dev.myexpense.ui.expense.ExpenseViewModel

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    viewModel: ExpenseViewModel = viewModel(factory = ExpenseViewModel.Factory)
): AppState{

    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentPage = remember {
        derivedStateOf {
            backStackEntry.value?.destination?.run {
                if(hasRoute(Page.ExpenseDetails::class))
                    return@derivedStateOf backStackEntry
                        .value!!.toRoute<Page.ExpenseDetails>()
            }
            Page.ExpenseList
        }
    }

    return remember {
        AppState(
            viewModel = viewModel,
            navController = navController,
            currentPage = currentPage
        )
    }
}

class AppState (
    val viewModel: ExpenseViewModel,
    val navController: NavHostController,
    val currentPage: State<Page>
)