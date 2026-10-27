package com.superheeyoung.feature.inputamount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.superheeyoung.feature.inputamount.InputAmountContract.Event
import java.math.BigDecimal

@Composable
fun InputAmountScreen(
    modifier: Modifier = Modifier,
    viewModel: InputAmountViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    InputAmountScreenContent(
        modifier = modifier,
        state = state,
        emitEvent = viewModel::setEvent
    )
}

@Composable
private fun InputAmountScreenContent(
    state: InputAmountContract.State,
    emitEvent: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text(
            text = "Amount input",
            style = MaterialTheme.typography.headlineSmall
        )

        AmountModeSelector(
            selected = state.inputMode,
            onSelected = { emitEvent(Event.OnModeChanged(it)) }
        )

        AmountInputField(
            inputAmount = state.inputAmount,
            formattedInputAmount = state.formattedInputAmount,
            formattedMultipliedAmount = state.formattedMultipliedAmount.orEmpty(),
            placeholder = state.placeholder,
            isAccountDeposit = state.isAccountDeposit,
            onAmountChanged = { emitEvent(Event.OnAmountChanged(it)) }
        )

        AmountSummary(state)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PlusAmounts.forEach { amount ->
                OutlinedButton(onClick = { emitEvent(Event.OnPlusAmountClick(amount)) }) {
                    Text("+${amount.toPlainString()}")
                }
            }
            OutlinedButton(onClick = { emitEvent(Event.OnClearClick) }) {
                Text("Clear")
            }
        }
    }
}

private val PlusAmounts = listOf(BigDecimal(10_000), BigDecimal(50_000), BigDecimal(100_000))

@Composable
private fun AmountModeSelector(
    selected: AmountInputMode,
    onSelected: (AmountInputMode) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AmountInputMode.entries.forEach { mode ->
            Button(
                onClick = { onSelected(mode) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (mode == selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    contentColor = if (mode == selected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            ) {
                Text(mode.label)
            }
        }
    }
}

@Composable
private fun AmountInputField(
    inputAmount: String,
    formattedInputAmount: String,
    formattedMultipliedAmount: String,
    placeholder: String,
    isAccountDeposit: Boolean,
    onAmountChanged: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val displayText = if (isFocused) formattedMultipliedAmount else formattedInputAmount
    val visualTransformation = remember(displayText, isAccountDeposit) {
        MultiplierAmountVisualTransformation(displayText, isAccountDeposit)
    }
    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = remember(textColor) {
        TextStyle(color = textColor, fontSize = 28.sp, textAlign = TextAlign.End)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "KRW",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { isFocused = it.isFocused },
            value = TextFieldValue(
                text = inputAmount,
                selection = TextRange(inputAmount.length)
            ),
            onValueChange = { onAmountChanged(it.text) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = visualTransformation,
            textStyle = textStyle,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterEnd) {
                    if (inputAmount.isEmpty()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = placeholder,
                            color = MaterialTheme.colorScheme.outline,
                            fontSize = 28.sp,
                            textAlign = TextAlign.End
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
private fun AmountSummary(state: InputAmountContract.State) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SummaryRow(label = "Raw input", value = state.inputAmount.ifEmpty { "-" })
        SummaryRow(
            label = "Calculated amount",
            value = state.formattedMultipliedAmount ?: "-"
        )
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            modifier = Modifier.weight(1f),
            text = value,
            textAlign = TextAlign.End
        )
    }
}

/**
 * 입력한 raw 값 대신 배수가 적용된 금액을 보여주고, 커서는 배수로 붙는 0 앞에 둔다.
 * 계좌 입금은 끝에서 2칸(x100), 현금 입금은 끝에서 5칸(x10,000 + 콤마) 앞이다.
 */
private class MultiplierAmountVisualTransformation(
    private val displayText: String,
    private val isAccountDeposit: Boolean
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val displayed = if (text.text.isEmpty()) "" else displayText
        val offsetFromEnd = if (isAccountDeposit) 2 else 5
        val cursorPosition = (displayed.length - offsetFromEnd).coerceAtLeast(0)
        val rawLength = text.text.length
        return TransformedText(
            AnnotatedString(displayed),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    if (rawLength == 0 || cursorPosition == 0) return 0
                    val clamped = offset.coerceIn(0, rawLength)
                    return ((clamped.toLong() * cursorPosition) / rawLength)
                        .toInt()
                        .coerceIn(0, cursorPosition)
                }

                override fun transformedToOriginal(offset: Int): Int {
                    if (rawLength == 0 || cursorPosition == 0) return 0
                    val safeOffset = offset.coerceAtLeast(0)
                    if (safeOffset >= cursorPosition) return rawLength
                    return ((safeOffset.toLong() * rawLength) / cursorPosition)
                        .toInt()
                        .coerceIn(0, rawLength)
                }
            }
        )
    }
}
