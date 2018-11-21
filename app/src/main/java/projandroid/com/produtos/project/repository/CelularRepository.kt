package projandroid.com.produtos.project.repository

import android.arch.lifecycle.LiveData
import projandroid.com.produtos.project.db.Celular
import projandroid.com.produtos.project.db.CelularDao

class CelularRepository(private val celularDAO: CelularDao) {

    val listaCelulares: LiveData<List<Celular>> = celularDAO.getAll()

    fun insert(celular: Celular) {
        celularDAO.insert(celular)
    }
}