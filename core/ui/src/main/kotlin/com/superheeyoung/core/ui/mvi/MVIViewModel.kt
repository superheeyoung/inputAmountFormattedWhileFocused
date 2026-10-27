package com.superheeyoung.core.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface UiState

/**
 * @return debounce debounce time in ms, ex) input text 같은 경우 0 으로 설정
 */
interface UiEvent {
    fun debounce(): Long = 300
}

interface UiEffect

abstract class MVIViewModel<Event : UiEvent, State : UiState, Effect : UiEffect>(
    initialState: State
) : ViewModel() {

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()
    val currentState: State get() = _uiState.value

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        collectEvent()
    }

    protected fun setState(reduce: State.() -> State) {
        _uiState.value = currentState.reduce()
    }

    @OptIn(FlowPreview::class)
    private fun collectEvent() = viewModelScope.launch {
        _event
            .debounce { it.debounce() }
            .collect { handleEvent(it) }
    }

    protected abstract fun handleEvent(event: Event)

    fun setEvent(event: Event) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    protected fun setEffect(effect: Effect) = viewModelScope.launch {
        _effect.send(effect)
    }
}
