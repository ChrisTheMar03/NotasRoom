package com.christhemar.notasroom.db

import androidx.room.*

@Dao
interface NotaDAO {

    @Query("SELECT * FROM nota ORDER BY id DESC")
    suspend fun getAll():List<Nota>

    @Query("SELECT * FROM nota WHERE id=:id" )
    suspend fun findById(id:Int):Nota

    @Insert
    suspend fun insert(nota: Nota)

    @Update
    suspend fun update(nota: Nota)

    @Delete
    suspend fun delete(nota: Nota)

    @Query("SELECT MAX(id) FROM nota")
    suspend fun lastId():Int

}