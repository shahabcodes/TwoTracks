package com.shahaabapps.core.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing

/**
 * A Parallel text field: no box, only a 1.5dp line beneath the text, which
 * turns from hairline to ink while the field has focus, or red when [isError].
 *
 * An optional mono [label] sits above it, with [labelTrailing] opposite (a
 * "Forgot?" link), and [trailing] sits at the end of the line (a show-password
 * toggle). [visualTransformation] overrides what [isPassword] would choose.
 * [strong] keeps the line in ink, as the prototype draws a title field, and
 * [showLine] drops it for a field inside a row that already has a rule.
 */
@Composable
fun CupertinoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    isPassword: Boolean = false,
    label: String? = null,
    labelTrailing: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation? = null,
    onFocusChange: (Boolean) -> Unit = {},
    textStyle: TextStyle? = null,
    strong: Boolean = false,
    showLine: Boolean = true,
) {
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()
    LaunchedEffect(focused) { onFocusChange(focused) }
    val line = when {
        isError -> AppTheme.colors.destructive
        focused || strong -> AppTheme.colors.ink
        else -> AppTheme.colors.separator
    }
    val style = (textStyle ?: MaterialTheme.typography.bodyLarge).let { if (it.color == Color.Unspecified) it.copy(color = MaterialTheme.colorScheme.onSurface) else it }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        if (label != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = AppTheme.colors.secondaryLabel,
                    modifier = Modifier.weight(1f),
                )
                labelTrailing?.invoke()
            }
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = style,
            cursorBrush = SolidColor(AppTheme.colors.ink),
            keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else keyboardType),
            visualTransformation = visualTransformation ?: if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            interactionSource = interaction,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp)
                .then(if (label != null) Modifier.semantics { contentDescription = label } else Modifier)
                .drawBehind {
                    if (!showLine) return@drawBehind
                    val stroke = Spacing.rule.toPx()
                    drawLine(line, Offset(0f, size.height - stroke / 2), Offset(size.width, size.height - stroke / 2), stroke)
                },
            decorationBox = { inner ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 14.dp)) {
                    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.weight(1f)) {
                        if (value.isEmpty() && placeholder.isNotEmpty()) {
                            Text(placeholder, style = style, color = AppTheme.colors.secondaryLabel)
                        }
                        inner()
                    }
                    trailing?.invoke()
                }
            },
        )
    }
}
