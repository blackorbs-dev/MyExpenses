package blackorbs.dev.myexpense.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import blackorbs.dev.myexpense.entities.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(favorite: Expense): Long

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun get(id: Long): Expense

    @Update
    suspend fun update(favorite: Expense)

    @Delete
    suspend fun delete(favorite: Expense)

    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAll(): Flow<List<Expense>>
}