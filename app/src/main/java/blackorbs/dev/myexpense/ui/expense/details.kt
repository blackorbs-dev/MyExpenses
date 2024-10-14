package blackorbs.dev.myexpense.ui.expense

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import blackorbs.dev.myexpense.R
import blackorbs.dev.myexpense.entities.Expense
import blackorbs.dev.myexpense.formatPrice
import blackorbs.dev.myexpense.ui.theme.MyExpenseTheme
import coil3.compose.AsyncImage

@Composable
fun ExpenseDetails(
    id: Long,
    context: Context = LocalContext.current,
    viewModel: ExpenseViewModel =
        viewModel(context as ComponentActivity)
){
    val expense = viewModel.expense.value

    LaunchedEffect(id) {
        viewModel.get(id)
    }

    expense?.let {
        ExpenseDetailsLayout(expense = it)
    }
}

@Composable
fun ExpenseDetailsLayout(expense: Expense){
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primary
                    ),
                    startY = 600f
                )
            )
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Icon(
                painter = painterResource(R.drawable.ic_payment_24),
                contentDescription = expense.description,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .sizeIn(
                    minWidth = 80.dp, minHeight = 80.dp
                ).background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ).padding(10.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.price))
                Text(expense.price?.formatPrice()?:"0")
            }
            HorizontalDivider(
                modifier = Modifier.padding(
                    top = 4.dp, bottom = 4.dp
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.payment_option))
                Text(stringResource(
                    expense.splitOption?.textID
                        ?:R.string.paid_and_split_equally
                ))
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                expense.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBorderWithTitle(
                        R.string.description
                    ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            AsyncImage(
                model = expense.imageUri,
                contentDescription = expense.description,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.drawBorderWithTitle(
                    R.string.receipt
                )
            )
        }

    }
}

@Composable
fun Modifier.drawBorderWithTitle(
    @StringRes titleResId: Int,
    text: String = stringResource(titleResId),
    textMeasurer: TextMeasurer = rememberTextMeasurer(),
    style: TextStyle = MaterialTheme.typography.titleMedium,
    textLayoutResult: TextLayoutResult = remember(text) {
        textMeasurer.measure(
            text, style = style
        )
    },
    color: Color = MaterialTheme.colorScheme.primary,
    onColor: Color = MaterialTheme.colorScheme.onPrimary
) = padding(
    top = 16.dp, start = 6.dp, end = 6.dp, bottom = 6.dp
)
    .drawBehind {
        val w = size.width
        val tW = textLayoutResult.size.width
        val tH = textLayoutResult.size.height
        val bW = tW + tW * 0.3f
        val bH = tH + tH * 0.2f
        drawRoundRect(
            color = color,
            cornerRadius = CornerRadius(x = 24f),
            style = Stroke(width = 6f)
        )
        drawRoundRect(
            color = color,
            cornerRadius = CornerRadius(x = 24f),
            topLeft = Offset(
                x = w / 2 - bW / 2,
                y = -bH / 2
            ),
            size = Size(bW, bH)
        )
        drawText(
            textLayoutResult,
            topLeft = Offset(
                x = w / 2 - tW / 2,
                y = -tH / 2f
            ),
            color = onColor
        )
    }
    .padding(
        top = 12.dp,
        start = 6.dp,
        end = 6.dp,
        bottom = 6.dp
    )

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun ExpenseDetailsPreview(){
    MyExpenseTheme {
        Scaffold {
            ExpenseDetailsLayout(expense = Expense().apply {
                description = "Shopping on the highway"
                price = 6430
            })
        }
    }
}


