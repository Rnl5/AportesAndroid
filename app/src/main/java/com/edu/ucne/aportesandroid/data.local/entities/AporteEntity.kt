package com.edu.ucne.aportesandroid.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Aportes")
data class AporteEntity(
    @PrimaryKey
    val aporteId: Int? = null,
    var persona:String = "",
    var observacion:String = "",
    var fecha:String = "",
    var monto:Double? = null
)
