package blackorbs.dev.myexpense.entities

import android.net.Uri
import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import blackorbs.dev.myexpense.R

@Entity(tableName = "expenses")
data class Expense(
    var price: Long? = null,
    var description: String = "",
    var imageUri: Uri? = null,
    var splitOption: SplitOption? = null,
    var isNew: Boolean = true,
    @PrimaryKey(autoGenerate = true) var id: Long = 0L
)

class Converter{
    @TypeConverter
    fun uriToString(uri: Uri) = uri.toString()

    @TypeConverter
    fun stringToUri(string: String): Uri = Uri.parse(string)

    @TypeConverter
    fun splitOptionToString(splitOption: SplitOption) = splitOption.name

    @TypeConverter
    fun stringToSplitOption(string: String) = SplitOption.valueOf(string)
}

enum class SplitOption(@StringRes val textID: Int){
    YOU_PAID_AND_SPLIT_EQUALLY(R.string.paid_and_split_equally),
    GUEST_PAID_AND_SPLIT_EQUALLY(R.string.guest_paid_and_split_equally),
    OWED_FULL_AMOUNT(R.string.owed_full_amount),
    OWE_FULL_AMOUNT(R.string.owe_full_amount)
}