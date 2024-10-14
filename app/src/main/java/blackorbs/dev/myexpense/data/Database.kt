package blackorbs.dev.myexpense.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import blackorbs.dev.myexpense.entities.Converter
import blackorbs.dev.myexpense.entities.Expense

@Database(
    entities = [Expense::class],
    version = 1,
)
@TypeConverters(Converter::class)
abstract class Database: RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}