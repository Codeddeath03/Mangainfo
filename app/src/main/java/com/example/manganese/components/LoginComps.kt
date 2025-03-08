package com.example.manganese.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.manganese.ui.theme.ManganeseTheme


@Composable
fun LoginTextField(
    value:String,
    fieldName:String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    leadingIcon:ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label={ Text(fieldName) },
        leadingIcon={if(leadingIcon != null) Icon(leadingIcon, contentDescription = null)},
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        shape= RoundedCornerShape(30),
        maxLines=1
    )
}


@Composable
fun HeaderText(text:String,
                       modifier: Modifier = Modifier,fontSize:Int? = 32
) {
    Text(text, modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Left,
        style= TextStyle(
            fontSize = fontSize?.sp ?: 32.sp,
        ),
        fontWeight = FontWeight.Bold
    )
}

@Preview(showSystemUi = true)
@Composable
private fun PrevHeaderText() {
    ManganeseTheme {
        HeaderText("Login", modifier = Modifier.padding(32.dp))
    }
}