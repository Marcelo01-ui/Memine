package com.example.lasttime.data.repository

import com.example.lasttime.data.remote.CalendarApiService
import com.example.lasttime.data.remote.HolidayDto
import com.example.lasttime.domain.model.ExternalDataException
import com.example.lasttime.domain.model.Holiday
import io.ktor.client.plugins.ResponseException
import java.io.IOException
import java.time.LocalDate
import kotlin.coroutines.cancellation.CancellationException

class CalendarRepository(private val api: CalendarApiService) {

    /** Retorna todos os feriados para o ano informado (API ou fallback offline). */
    suspend fun getHolidaysForYear(year: Int): List<Holiday> =
        try {
            api.getPublicHolidays(year).toHolidays()
        } catch (e: Exception) {
            // Fallback com os feriados nacionais fixos do Brasil
            listOf(
                Holiday(LocalDate.of(year, 1, 1), "Confraternização Universal"),
                Holiday(LocalDate.of(year, 4, 21), "Tiradentes"),
                Holiday(LocalDate.of(year, 5, 1), "Dia do Trabalhador"),
                Holiday(LocalDate.of(year, 9, 7), "Independência do Brasil"),
                Holiday(LocalDate.of(year, 10, 12), "Nossa Senhora Aparecida"),
                Holiday(LocalDate.of(year, 11, 2), "Finados"),
                Holiday(LocalDate.of(year, 11, 15), "Proclamação da República"),
                Holiday(LocalDate.of(year, 11, 20), "Dia da Consciência Negra"),
                Holiday(LocalDate.of(year, 12, 25), "Natal")
            ).sortedBy { it.date }
        }

    /** Próximo feriado a partir de hoje. */
    suspend fun getNextHoliday(today: LocalDate): Result<Holiday?> =
        try {
            val thisYear = getHolidaysForYear(today.year)
            val next = thisYear.firstOrNull { !it.date.isBefore(today) }
                ?: getHolidaysForYear(today.year + 1).firstOrNull()
            Result.success(next)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(ExternalDataException(e.toFriendlyMessage(), e))
        }

    private fun List<HolidayDto>.toHolidays(): List<Holiday> =
        map { Holiday(date = LocalDate.parse(it.date), name = it.localName) }.sortedBy { it.date }

    private fun Exception.toFriendlyMessage(): String = when (this) {
        is ResponseException -> "O serviço de feriados está indisponível no momento."
        is IOException -> "Sem conexão com a internet ou tempo esgotado."
        else -> "Resposta inválida do serviço de feriados."
    }
}
