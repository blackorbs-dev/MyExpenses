package blackorbs.dev.myexpense

import android.content.Context
import androidx.room.Room
import blackorbs.dev.myexpense.data.Database

class AppModule(private val context: Context) {

    private val database by lazy {
        Room.databaseBuilder(
            context, klass = Database::class.java, "app_data"
        ).build()
    }

    val expenseDao by lazy {
        database.expenseDao()
    }
}