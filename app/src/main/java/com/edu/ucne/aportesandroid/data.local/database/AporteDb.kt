package com.edu.ucne.aportesandroid.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.edu.ucne.aportesandroid.data.local.dao.AporteDao
import com.edu.ucne.aportesandroid.data.local.entities.AporteEntity


@Database(
    entities = [
        AporteEntity::class
    ],
    version = 2,
    exportSchema = false
)


abstract class AporteDb : RoomDatabase() {
    abstract fun aporteDao() : AporteDao
}