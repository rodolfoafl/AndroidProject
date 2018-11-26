package projandroid.com.filmes.project.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface FilmeDao {

    @Insert
    fun insert(filme: Filme)

    @Update
    fun update(filme: Filme)

    @Delete
    fun delete(filme: Filme)

    @Query("DELETE FROM filmes")
    fun deleteAll()

    @Query("SELECT * FROM filmes")
    fun getAll():LiveData<List<Filme>>
}