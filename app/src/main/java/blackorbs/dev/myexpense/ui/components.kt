package blackorbs.dev.myexpense.ui

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import blackorbs.dev.myexpense.R
import blackorbs.dev.myexpense.entities.SplitOption
import java.io.File

@Composable
internal fun OutlineEditBox(
    text: String,
    @StringRes labelResId: Int,
    maxLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onTextChanged: (String) -> Unit
){
    var newText by remember { mutableStateOf(text) }

    OutlinedTextField(
        value = newText,
        onValueChange = {
            newText = it
            onTextChanged(it)
        },
        shape = RoundedCornerShape(
            topEnd = 16.dp,
            bottomStart = 16.dp
        ),
        singleLine = maxLines == 1,
        maxLines = maxLines,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 10.dp, end = 10.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType =  keyboardType,
            imeAction = imeAction
        ),
        label = {Text(stringResource(labelResId))}
    )
}

@Composable
internal fun PopupDialog(
    @StringRes titleRes: Int,
    onDismiss: () -> Unit,
    horizontalAlignment: Alignment.Horizontal = Alignment.End,
    content: @Composable (ColumnScope.() -> Unit)
){
    Dialog(onDismissRequest = onDismiss) {
        Card(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(horizontalAlignment = horizontalAlignment) {
                Text(
                    stringResource(titleRes),
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineSmall
                )
                content()
            }
        }
    }
}

@Composable
internal fun RowActionButtons(
    modifier: Modifier = Modifier,
    @StringRes leftTitleRes: Int,
    @StringRes rightTileRes: Int,
    onLeftClicked: () -> Unit,
    onRightClicked: () -> Unit
){
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(
            onClick = onLeftClicked,
            Modifier.padding(start = 10.dp, bottom = 5.dp)
        ) {
            Text(
                stringResource(leftTitleRes),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Button(
            onClick = onRightClicked,
            Modifier.padding(end = 10.dp, bottom = 5.dp),
            colors = ButtonDefaults.buttonColors().copy(
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                stringResource(rightTileRes)
            )
        }
    }
}

@Composable
fun ImageSelector(
    context: Context = LocalContext.current,
    onSelected: (Uri) -> Unit
){
    var imageText by remember{
        mutableStateOf<String?>(null)
    }
    OutlinedCard(
        modifier = Modifier.padding(
            horizontal = 10.dp, vertical = 5.dp
        ),
        shape = RoundedCornerShape(
            topEnd = 16.dp,
            bottomStart = 16.dp
        ),
        colors = CardDefaults.cardColors()
    ) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = {
                if(it != null){
                    context.contentResolver.takePersistableUriPermission(
                        it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    imageText = context.getFileName(it)
                    onSelected(it)
                }
            }
        )
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(5.dp).fillMaxWidth()
        ){
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = stringResource(R.string.upload_receipt),
            )
            FilledIconButton(
                modifier = Modifier.padding(end = 5.dp),
                onClick = {
                    launcher.launch(PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    ))
                }
            ) {
                Icon(
                    painterResource(R.drawable.ic_add_a_photo_24),
                    stringResource(R.string.upload_receipt)
                )
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary
        )
        imageText?.let { Text(it,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodySmall
        ) }
    }
}

fun Context.getFileName(uri: Uri): String? = when(uri.scheme){
    ContentResolver.SCHEME_CONTENT -> getContentFileName(uri)
    else -> uri.path?.let(::File)?.name
}

fun Context.getContentFileName(uri: Uri): String? = kotlin.runCatching {
    contentResolver.query(
        uri, null, null, null, null
    )?.use { cursor ->
        cursor.moveToFirst()
        return@use cursor.getColumnIndexOrThrow(
            OpenableColumns.DISPLAY_NAME
        ).let(cursor::getString)
    }
}.getOrNull()

@Composable
fun SplitOptionSelector(
    onValueChanged: (SplitOption) -> Unit
){
    val items = SplitOption.entries.toList()
    var showMenu by remember {mutableStateOf(false)}
    var selectIndex by remember { mutableIntStateOf(-1) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp, bottom = 10.dp,
                start = 10.dp, end = 10.dp
            ),
        shape = RoundedCornerShape(
            topEnd = 16.dp,
            bottomStart = 16.dp
        ),
        colors = TextFieldDefaults.colors(), readOnly = true,
        maxLines = 1, singleLine = true,
        value = stringResource(
            if(selectIndex == -1)
                R.string.select_split_option
            else items[selectIndex].textID
        ),
        onValueChange = {},
        placeholder = { Text(stringResource(R.string.select_split_option)) },
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            showMenu = !showMenu
                        }
                    }
                }
            },
        trailingIcon = {
                Icon(
                    if(showMenu) painterResource(R.drawable.ic_round_arrow_drop_up_24)
                    else painterResource(R.drawable.ic_round_arrow_drop_down_24),
                    stringResource(R.string.select_split_option),
                    Modifier.size(50.dp)
                )
        }
    )
    if(showMenu){
        Dialog(onDismissRequest = {showMenu = false}) {
            OutlinedCard(
                Modifier.fillMaxWidth(),
                RoundedCornerShape(20.dp),
                CardDefaults.cardColors()
            ) {
                Column{
                    items.forEachIndexed { index, item ->
                        DropDownItem(
                            stringResource(item.textID),
                            isSelected = index == selectIndex
                        ) {
                            selectIndex = index
                            onValueChanged(item)
                            showMenu = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropDownItem(text: String, isSelected: Boolean, onClick: ()->Unit){
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 7.dp)
                .clickable { onClick() },
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text(text)
            RadioButton(isSelected, onClick)
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}


