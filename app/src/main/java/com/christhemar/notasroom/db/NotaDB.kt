package com.christhemar.notasroom.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Nota::class],
    version = 1,
    exportSchema = false
)
abstract class NotaDB : RoomDatabase() {

    abstract val notaDao:NotaDAO

    companion object{
        const val DB_NAME="dbnotas"
    }

}