package com.edu.ucne.aportesandroid
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog

import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.Room
import com.edu.ucne.aportesandroid.data.local.database.AporteDb
import com.edu.ucne.aportesandroid.data.local.entities.AporteEntity
import com.edu.ucne.aportesandroid.presentation.AporteListScreen
import com.edu.ucne.aportesandroid.ui.theme.AportesAndroidTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private lateinit var aporteDb: AporteDb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        aporteDb = Room.databaseBuilder(
            this,
            AporteDb::class.java,
            "Aporte.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        enableEdgeToEdge()
        setContent {
            AportesAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(8.dp)
                    ) {
                        val aportes: List<AporteEntity> by getAportes().collectAsStateWithLifecycle(
                            initialValue = emptyList()
                        )

                        var aporteId by remember { mutableStateOf("") }
                        var persona by remember { mutableStateOf("") }
                        var observacion by remember { mutableStateOf("") }
                        var fecha by remember { mutableStateOf("") }
                        var monto by remember { mutableStateOf("") }

                        var mostrarDatePicker by remember { mutableStateOf(false) }

                        val state = rememberDatePickerState()

                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {


                                OutlinedTextField(
                                    label = { Text(text = "Persona") },
                                    value = persona,
                                    onValueChange = { persona = it },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    label = { Text(text = "Observacion") },
                                    value = observacion,
                                    onValueChange = { observacion = it },
                                    modifier = Modifier.fillMaxWidth()
                                )


                                OutlinedTextField(
                                    label = { Text(text = "Fecha") },
                                    value = fecha,
                                    readOnly = true,
                                    onValueChange = { },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    trailingIcon = {
                                        IconButton(
                                            onClick = {
                                                mostrarDatePicker  = true
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.DateRange,
                                                contentDescription = "Date Picker"
                                            )
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()

                                        .clickable(enabled = true) {
                                            mostrarDatePicker  = true
                                        }
                                )


                                OutlinedTextField(
                                    label = { Text(text = "Monto") },
                                    value = monto,
                                    onValueChange = { monto = it },
                                    modifier = Modifier.fillMaxWidth()
                                )


                                Spacer(modifier = Modifier.padding(2.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            aporteId = ""
                                            persona = ""
                                            observacion = ""
                                            fecha = ""
                                            monto = "0.0"
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "nuevo boton"
                                        )
                                        Text(text = "Nuevo")
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            saveAporte(
                                                AporteEntity(
                                                    aporteId = aporteId.toIntOrNull(),
                                                    persona = persona,
                                                    observacion = observacion,
                                                    fecha = fecha,
                                                    monto = monto.toDoubleOrNull() ?: 0.0
                                                )
                                            )

                                            aporteId = ""
                                            persona = ""
                                            observacion = ""
                                            fecha = ""
                                            monto = "0.0"
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "save button"
                                        )

                                        Text(text = "Guardar")
                                    }




                                    if (mostrarDatePicker) {
                                        DatePickerDialog(
                                            onDismissRequest = { mostrarDatePicker = false },
                                            confirmButton = {
                                                Button(
                                                    onClick = {
                                                        val selectedDate = state.selectedDateMillis
                                                        if (selectedDate != null) {
                                                            val selectedInstant = Instant.ofEpochMilli(selectedDate)
                                                            val selectedLocalDate = selectedInstant.atZone(ZoneId.systemDefault()).toLocalDate()
                                                            fecha = selectedLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                                        }
                                                        // Código para guardar la fecha seleccionada...
                                                        mostrarDatePicker = false
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color.Blue,
                                                        contentColor = Color.White
                                                    )
                                                ) {
                                                    Text(text = "Aceptar")
                                                }
                                            },
                                            dismissButton = {
                                                OutlinedButton(onClick = { mostrarDatePicker = false }) {
                                                    Text(text = "Cancelar")
                                                }
                                            }
                                        ) {
                                            DatePicker(
                                                state = state,
                                            )
                                        }
                                    }
                                }
                            }

                        }

                        Spacer(modifier = Modifier.padding(2.dp))

                        AporteListScreen(
                            aportes = aportes,
                            onVerAporte = { aporteSeleccionado ->
                                aporteId = aporteSeleccionado.aporteId.toString()
                                persona = aporteSeleccionado.persona
                                //observacion = aporteSeleccionado.observacion
                                fecha = aporteSeleccionado.fecha
                                monto = aporteSeleccionado.monto?.toString() ?: "0.0"
                            },
                            onBorrarAporte = { aporte ->
                                borrarAporte(aporte)
                            })
                    }
                }
            }
        }
    }

    fun saveAporte(aporte: AporteEntity) {
        GlobalScope.launch {
            aporteDb.aporteDao().save(aporte)
        }
    }

    fun getAportes(): Flow<List<AporteEntity>> {
        return aporteDb.aporteDao().getAll()
    }

    fun borrarAporte(aporte: AporteEntity) {
        GlobalScope.launch {
            aporteDb.aporteDao().delete(aporte)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    AportesAndroidTheme {

    }
}