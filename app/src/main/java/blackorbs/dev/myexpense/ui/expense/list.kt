package blackorbs.dev.myexpense.ui.expense

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import blackorbs.dev.myexpense.Page
import blackorbs.dev.myexpense.R
import blackorbs.dev.myexpense.entities.Expense
import blackorbs.dev.myexpense.formatPrice
import blackorbs.dev.myexpense.ui.ActionType
import blackorbs.dev.myexpense.ui.ImageSelector
import blackorbs.dev.myexpense.ui.OutlineEditBox
import blackorbs.dev.myexpense.ui.RowActionButtons
import blackorbs.dev.myexpense.ui.SplitOptionSelector
import blackorbs.dev.myexpense.ui.theme.MyExpenseTheme

fun NavGraphBuilder.expensePage(
    onShowDetails: (Long) -> Unit,
    onEdit: (Expense) -> Unit
){
    composable<Page.ExpenseList> {
        ExpenseList(
            onShowDetails = onShowDetails,
            onEdit = onEdit
        )
    }
    composable<Page.ExpenseDetails>(
        exitTransition = {
            slideOutOfContainer(
                animationSpec = tween(500),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        },
        enterTransition = {
            slideIntoContainer(
                animationSpec = tween(500),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        }
    ) {
        ExpenseDetails(
            it.toRoute<Page.ExpenseDetails>().id
        )
    }
}

@Composable
fun ExpenseList(
    context: Context = LocalContext.current,
    viewModel: ExpenseViewModel =
        viewModel(context as ComponentActivity),
    onShowDetails: (Long) -> Unit,
    onEdit: (Expense) -> Unit
){
    val expensesList = viewModel.allExpenses.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(
            brush = Brush.verticalGradient(
                listOf(
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.colorScheme.primary
                ),
                startY = 600f
            )
        )
    ){
        items(
            items = expensesList.value,
            key = {it.id}
        ){
            ExpenseItem(
                modifier = Modifier.animateItem(),
                expense = it,
                onShowDetails = {
                    onShowDetails(it.id)
                },
                onEdit = {
                    onEdit(it)
                }
            )
        }
    }
}

@Composable
fun ExpenseItem(
    modifier: Modifier = Modifier,
    expense: Expense,
    onShowDetails: () -> Unit,
    onEdit: () -> Unit
){
    var isExpanded by remember {
        mutableStateOf(false)
    }
    ElevatedCard(
        modifier = modifier.padding(
            horizontal = 8.dp, vertical = 2.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(
                painter = painterResource(
                    R.drawable.ic_payment_24
                ),
                contentDescription = expense.description,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .sizeIn(
                        minWidth = 42.dp, minHeight = 42.dp
                    )
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(
                        R.string.item_title,
                        (expense.price?:0).formatPrice()
                    ),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    expense.description, maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Box {
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = stringResource(R.string.update)
                    )
                }
                DropdownMenu(
                    expanded = isExpanded,
                    modifier = Modifier.align(Alignment.TopEnd),
                    onDismissRequest = { isExpanded = false }
                ) {
                    listOf(
                        Pair(R.string.view_details, R.drawable.ic_info_24),
                        Pair(R.string.edit_expense, R.drawable.ic_edit_square_24)
                    )
                        .forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(stringResource(item.first)) },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(item.second),
                                        contentDescription = stringResource(item.first)
                                    )
                                },
                                onClick = {
                                    if (index == 0) {
                                        onShowDetails()
                                    }
                                    else { onEdit() }
                                    isExpanded = false
                                }
                            )
                        }
                }
            }
        }
    }
}

@Composable
fun ExpenseEditLayout(
    expense: Expense,
    onAction: (ActionType) -> Unit
){
    OutlineEditBox(
        text = expense.description,
        labelResId = R.string.description
    ) {
        expense.description = it
    }
    OutlineEditBox(
        text = if(expense.price == null) ""
        else expense.price.toString(),
        labelResId = R.string.price,
        keyboardType = KeyboardType.Number
    ) {
        expense.price = it.toLongOrNull()
    }
    SplitOptionSelector {
        expense.splitOption = it
    }
    ImageSelector {
        expense.imageUri = it
    }
    Spacer(modifier = Modifier.height(16.dp))
    if(expense.isNew){
        Button(
            onClick = {
                onAction(ActionType.Add)
            },
            Modifier.padding(end = 10.dp, bottom = 5.dp),
            colors = ButtonDefaults.buttonColors().copy(
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                stringResource(R.string.add)
            )
        }
    }
    else{
        RowActionButtons(
            leftTitleRes = R.string.delete,
            rightTileRes = R.string.update,
            onLeftClicked = {
                onAction(ActionType.Delete)
            },
            onRightClicked = {
                onAction(ActionType.Update)
            }
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun ExpenseEditLayPreview(){
    MyExpenseTheme {
        Scaffold {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                ExpenseItem(
                    expense = Expense().apply {
                        price = 5000
                        description = "Shopping at lake side"
                    },
                    onShowDetails = { /*TODO*/ })
                {}
                ExpenseEditLayout(
                    expense = Expense().apply {
                        isNew = false
                    }
                ) {

                }
            }

        }
    }
}
