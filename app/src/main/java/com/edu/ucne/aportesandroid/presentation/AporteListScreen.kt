package com.edu.ucne.aportesandroid.presentation


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edu.ucne.aportesandroid.data.local.database.AporteDb
import com.edu.ucne.aportesandroid.data.local.entities.AporteEntity


@Composable
fun AporteListScreen(
    aportes: List<AporteEntity>,
    onVerAporte: (AporteEntity) -> Unit,
    onBorrarAporte: (AporteEntity) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(aportes) { aporte ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        //.clickable { onVerAporte(aporte) }
                        .padding(16.dp)
                ) {
                    Text(text = aporte.aporteId.toString(), modifier = Modifier.weight(0.10f))
                    Text(text = aporte.persona, modifier = Modifier.weight(0.400f))
                    //Text(text = aporte.observacion, modifier = Modifier.weight(0.40f))
                    Text(text = aporte.fecha, modifier = Modifier.weight(0.50f))
                    Text(text = aporte.monto.toString(), modifier = Modifier.weight(0.40f))



                    IconButton(
                        onClick = { onBorrarAporte(aporte) },
                        modifier = Modifier.weight(0.10f)
                    ) {
                        Icon(imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar Aporte")

                    }
                }
            }
        }
    }
}