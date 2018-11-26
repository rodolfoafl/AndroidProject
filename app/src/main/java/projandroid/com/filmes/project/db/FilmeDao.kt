package projandroid.com.filmes.project.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface FilmeDao {

    @Insert
    fun insert(filme: Filme)

    @Query("DELETE FROM filmes")
    fun deleteAll()

    @Query("SELECT * FROM filmes")
    fun getAll():LiveData<List<Filme>>
}