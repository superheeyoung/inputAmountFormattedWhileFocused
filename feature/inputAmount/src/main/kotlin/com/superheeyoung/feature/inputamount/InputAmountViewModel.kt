package com.superheeyoung.feature.inputamount

import com.superheeyoung.core.ui.mvi.MVIViewModel
import com.superheeyoung.feature.inputamount.InputAmountContract.Effect
import com.superheeyoung.feature.inputamount.InputAmountContract.Event
import com.superheeyoung.feature.inputamount.InputAmountContract.State
import java.math.BigDecimal

class InputAmountViewModel : MVIViewModel<Event, State, Effect>(State()) {

    override fun handleEvent(event: Event) = when (event) {
        is Event.OnAmountChanged -> setState { copy(inputAmount = normalize(event.inputAmount)) }

        is Event.OnPlusAmountClick -> setState {
            val amount = (multipliedAmount ?: BigDecimal.ZERO) + event.plusAmount
            copy(inputAmount = normalize(inputMode.toRawAmount(amount)))
        }

        is Event.OnModeChanged -> setState { copy(inputMode = event.mode) }
        Event.OnClearClick -> setState { copy(inputAmount = "") }
    }

    private fun normalize(value: String): String = value.filter(Char::isDigit).trimStart('0')
}
