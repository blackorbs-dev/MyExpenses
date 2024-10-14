package blackorbs.dev.myexpense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import blackorbs.dev.myexpense.ui.MainScreen
import blackorbs.dev.myexpense.ui.theme.MyExpenseTheme
import kotlinx.serialization.Serializable
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyExpenseTheme {
                MainScreen()
            }
        }
    }
}

fun Long.formatPrice(): String =
    NumberFormat.getCurrencyInstance()
        .apply {
            maximumFractionDigits = 0
        }.format(this)

@Serializable sealed interface Page {
    @Serializable
    data object ExpenseList: Page

    @Serializable
    data class ExpenseDetails(val id: Long): Page
}
