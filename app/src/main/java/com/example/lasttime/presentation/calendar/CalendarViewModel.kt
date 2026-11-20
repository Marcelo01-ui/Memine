package com.example.lasttime.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lasttime.data.repository.CalendarRepository
import com.example.lasttime.domain.model.Holiday
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsável por carregar e disponibilizar os feriados para a tela do Calendário.
 */
class CalendarViewModel(
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    private val _holidays = MutableStateFlow<List<Holiday>>(emptyList())
    val holidays: StateFlow<List<Holiday>> = _holidays.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Carrega a lista de feriados para um ano específico.
     */
    fun loadHolidaysForYear(year: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _holidays.value = calendarRepository.getHolidaysForYear(year)
            _isLoading.value = false
        }
    }
}
