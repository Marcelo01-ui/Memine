package com.example.lasttime.presentation.calendar

import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.lasttime.presentation.components.toBr
import com.example.lasttime.ui.theme.AppBackground
import com.example.lasttime.ui.theme.DarkText
import com.example.lasttime.ui.theme.GrayText
import com.example.lasttime.ui.theme.PrimaryNavy
import java.time.LocalDate

/**
 * Tela do Calendário integrando o componente nativo CalendarView do Android Studio (android.widget.CalendarView).
 *
 * Ao selecionar uma data no bloquinho do calendário, um botão "Adicionar Atividade Nesta Data" é exibido,
 * permitindo abrir o formulário já com a data pré-selecionada.
 *
 * @param onDayClick Callback acionado ao tocar em uma data do calendário.
 * @param onAddActivityForDate Callback para abrir o modal de nova atividade injetando a data selecionada.
 */
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    onDayClick: (LocalDate) -> Unit = {},
    onAddActivityForDate: (LocalDate) -> Unit = {}
) {
    // Guarda a data selecionada no CalendarView
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(top = 16.dp, start = 12.dp, end = 12.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 500.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card contendo o CalendarView nativo do Android Studio
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    // Renderiza o CalendarView nativo do Android Studio no Jetpack Compose
                    AndroidView(
                        factory = { context ->
                            CalendarView(context).apply {
                                setOnDateChangeListener { _, year, month, dayOfMonth ->
                                    val date = LocalDate.of(year, month + 1, dayOfMonth)
                                    selectedDate = date
                                    onDayClick(date)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Exibição da data selecionada e botão de adicionar atividade
            if (selectedDate != null) {
                val date = selectedDate!!

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Data Selecionada no Calendário:",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = date.toBr(),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium,
                            color = DarkText
                        )

                        Spacer(Modifier.height(12.dp))

                        // Botão para adicionar atividade já com esta data injetada
                        Button(
                            onClick = { onAddActivityForDate(date) },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Adicionar Atividade",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "Adicionar Atividade Nesta Data",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Selecione uma data no Calendário\npara adicionar uma atividade",
                        color = GrayText,
                        fontSize = 18.sp,
                        lineHeight = 26.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}
