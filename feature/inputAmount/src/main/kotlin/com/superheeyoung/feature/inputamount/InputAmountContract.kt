package com.superheeyoung.feature.inputamount

import com.superheeyoung.core.ui.mvi.UiEffect
import com.superheeyoung.core.ui.mvi.UiEvent
import com.superheeyoung.core.ui.mvi.UiState
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private val MULTIPLIER_HUNDRED = BigDecimal(100)
private val MULTIPLIER_THOUSAND = BigDecimal(1000)
private val MULTIPLIER_TEN_THOUSAND = BigDecimal(10000)

private val AmountFormat = DecimalFormat("#,###", DecimalFormatSymbols(Locale.KOREA))

private fun BigDecimal.formatWithGrouping(): String = synchronized(AmountFormat) {
    AmountFormat.format(this)
}

enum class AmountInputMode(val label: String, val placeholder: String) {
    AccountDeposit("Account", "1,000"),
    CashDeposit("Cash", "10,000");

    fun multiplier(rawAmount: String): BigDecimal = when (this) {
        CashDeposit -> MULTIPLIER_TEN_THOUSAND
        AccountDeposit ->
            if (rawAmount.length == 1) MULTIPLIER_THOUSAND else MULTIPLIER_HUNDRED
    }

    fun toRawAmount(amount: BigDecimal): String {
        val multiplier = when {
            this == CashDeposit -> MULTIPLIER_TEN_THOUSAND
            amount.isSingleDigitMultipleOf(MULTIPLIER_THOUSAND) -> MULTIPLIER_THOUSAND
            else -> MULTIPLIER_HUNDRED
        }
        return amount.divideToIntegralValue(multiplier).toPlainString()
    }

    private fun BigDecimal.isSingleDigitMultipleOf(base: BigDecimal): Boolean {
        val quotient = divideToIntegralValue(base)
        return remainder(base) == BigDecimal.ZERO && quotient in BigDecimal.ONE..BigDecimal(9)
    }
}

object InputAmountContract {
    /**
     * 입력 원본(inputMode, inputAmount)만 저장하고 나머지는 여기서 파생한다.
     * 파생 값은 State 인스턴스당 한 번만 계산한다.
     */
    data class State(
        val inputMode: AmountInputMode = AmountInputMode.AccountDeposit,
        val inputAmount: String = ""
    ) : UiState {
        val isAccountDeposit: Boolean
            get() = inputMode == AmountInputMode.AccountDeposit

        val placeholder: String
            get() = inputMode.placeholder

        val multipliedAmount: BigDecimal? by lazy(LazyThreadSafetyMode.NONE) {
            inputAmount.toBigDecimalOrNull()?.multiply(inputMode.multiplier(inputAmount))
        }

        val formattedMultipliedAmount: String? by lazy(LazyThreadSafetyMode.NONE) {
            multipliedAmount?.formatWithGrouping()
        }

        val formattedInputAmount: String by lazy(LazyThreadSafetyMode.NONE) {
            inputAmount.toBigDecimalOrNull()?.formatWithGrouping().orEmpty()
        }
    }

    sealed class Event : UiEvent {
        data class OnAmountChanged(val inputAmount: String) : Event() {
            override fun debounce(): Long = 0
        }

        data class OnPlusAmountClick(val plusAmount: BigDecimal) : Event() {
            override fun debounce(): Long = 0
        }

        data class OnModeChanged(val mode: AmountInputMode) : Event() {
            override fun debounce(): Long = 0
        }

        data object OnClearClick : Event() {
            override fun debounce(): Long = 0
        }
    }

    sealed class Effect : UiEffect
}
