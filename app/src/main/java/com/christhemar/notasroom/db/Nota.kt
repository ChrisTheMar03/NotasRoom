package com.christhemar.notasroom.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nota")
data class Nota(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id:Int,
    @ColumnInfo(name = "titulo")
    val titulo:String,
    @ColumnInfo(name = "texto")
    val texto:String,
    @ColumnInfo(name = "fecha")
    val fecha:String,
    @ColumnInfo(name = "color")
    val color:Int
)