package com.eltescode.modules.features.modules.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Subscript
import androidx.compose.material.icons.filled.Superscript
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor

@Composable
fun ModuleCommentTextRow(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    isIconEnabled: Boolean,
    isColorDropdownMenuVisible: Boolean,
    onValueChange: (String) -> Unit,
    onIconClick: () -> Unit,
    onColorButtonClick: () -> Unit,
    onColorMenuDismissRequest: () -> Unit,
) {
    val richTextState = rememberRichTextState()

    LaunchedEffect(key1 = Unit, block = {
        richTextState.setHtml(text)
    })

    LaunchedEffect(key1 = richTextState.annotatedString, block = {
        onValueChange(richTextState.toHtml())
    })
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$label:")
        if (isIconEnabled) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                modifier = Modifier.clickable { onIconClick() })
        } else {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                modifier = Modifier.clickable { onIconClick() })
        }
    }
    if (isIconEnabled) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            EditorPanelRow(
                richTextState = richTextState,
                modifier = Modifier.fillMaxWidth(),
                onBoldClick = {
                    richTextState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                },
                onItalicClick = {
                    richTextState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
                },
                onUnderLineClick = {
                    richTextState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                },
                onColorClick = {
                    richTextState.toggleSpanStyle(SpanStyle(color = it))
                },
                onColorButtonClick = onColorButtonClick,
                isColorDropdownMenuVisible = isColorDropdownMenuVisible,
                onColorMenuDismissRequest = onColorMenuDismissRequest,
                onSuperscriptClick = {
                    richTextState.toggleSpanStyle(
                        SpanStyle(
                            fontSize = 12.sp,
                            baselineShift = BaselineShift.Superscript
                        )
                    )
                },
                onSubscriptClick = {
                    richTextState.toggleSpanStyle(
                        SpanStyle(
                            fontSize = 12.sp,
                            baselineShift = BaselineShift.Subscript
                        )
                    )
                },
            )
            Spacer(modifier = Modifier.height(40.dp))
            RichTextEditor(
                state = richTextState,
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
            )
        }
    } else {
        Text(text = richTextState.annotatedString)
    }
}

@Composable
private fun EditorPanelRow(
    richTextState: RichTextState,
    modifier: Modifier = Modifier,
    onBoldClick: () -> Unit,
    onItalicClick: () -> Unit,
    onUnderLineClick: () -> Unit,
    onColorClick: (Color) -> Unit,
    onColorButtonClick: () -> Unit,
    onSuperscriptClick: () -> Unit,
    onSubscriptClick: () -> Unit,
    onColorMenuDismissRequest: () -> Unit,
    isColorDropdownMenuVisible: Boolean,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        EditorIconButton(
            onClick = onBoldClick,
            isToggled = richTextState.currentSpanStyle.fontWeight == FontWeight.Bold,
            icon = Icons.Default.FormatBold,
            contentDescription = null
        )

        EditorIconButton(
            onClick = onItalicClick,
            isToggled = richTextState.currentSpanStyle.fontStyle == FontStyle.Italic,
            icon = Icons.Default.FormatItalic,
            contentDescription = null
        )

        EditorIconButton(
            onClick = onUnderLineClick,
            isToggled = richTextState.currentSpanStyle.textDecoration == TextDecoration.Underline,
            icon = Icons.Default.FormatUnderlined,
            contentDescription = null
        )

        EditorIconButton(
            onClick = onSuperscriptClick,
            isToggled = richTextState.currentSpanStyle.baselineShift == BaselineShift.Superscript,
            icon = Icons.Default.Superscript,
            contentDescription = null
        )

        EditorIconButton(
            onClick = onSubscriptClick,
            isToggled = richTextState.currentSpanStyle.baselineShift == BaselineShift.Subscript,
            icon = Icons.Default.Subscript,
            contentDescription = null
        )

        ColorSelector(
            currentColor = richTextState.currentSpanStyle.color,
            onColorClick = onColorClick,
            onColorButtonClick = onColorButtonClick,
            isColorDropdownMenuVisible = isColorDropdownMenuVisible,
            onDismissRequest = onColorMenuDismissRequest
        )
    }
}

@Composable
private fun EditorIconButton(
    onClick: () -> Unit,
    isToggled: Boolean,
    icon: ImageVector,
    contentDescription: String?
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults
            .filledIconButtonColors(containerColor = if (isToggled) Color.Green else Color.White)
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}