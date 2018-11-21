package projandroid.com.produtos.project.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import projandroid.com.produtos.project.db.Celular
import projandroid.com.produtos.project.db.ProdutosDatabase
import projandroid.com.produtos.project.repository.CelularRepository
import kotlin.coroutines.experimental.CoroutineContext

class CelularViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CelularRepository
    val listaCelular: LiveData<List<Celular>>

    private var parentJob = Job()

    private val coroutineContext: CoroutineContext
    get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    init {
        val celularDao = ProdutosDatabase.
                getDatabase(application, scope).celularDAO()

        repository = CelularRepository(celularDao)
        listaCelular = repository.listaCelulares
    }

    fun insert(celular: Celular) = scope.launch(Dispatchers.IO){
        repository.insert(celular)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}