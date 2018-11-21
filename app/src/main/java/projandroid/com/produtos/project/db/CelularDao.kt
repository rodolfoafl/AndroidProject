package projandroid.com.produtos.project.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface CelularDao {

    @Insert
    fun insert(celular: Celular)

    @Query("DELETE FROM celulares")
    fun deleteAll()

    @Query("SELECT * FROM celulares")
    fun getAll():LiveData<List<Celular>>
}