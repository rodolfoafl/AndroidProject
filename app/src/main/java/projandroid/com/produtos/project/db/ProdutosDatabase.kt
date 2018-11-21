package projandroid.com.produtos.project.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.launch

@Database(entities = [Celular::class], version = 2)
abstract class ProdutosDatabase: RoomDatabase() {

    abstract fun celularDAO():CelularDao

    companion object {

        @Volatile
        private var INSTANCE: ProdutosDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope):ProdutosDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ProdutosDatabase::class.java,
                        "produtos-database"
                )
                        .fallbackToDestructiveMigration()
                        .addCallback(ProdutosDatabaseCallBack(scope))
                        .build()
                    INSTANCE = instance
                instance
            }
        }
    }

    private class ProdutosDatabaseCallBack(
            private val scope: CoroutineScope
    ): RoomDatabase.Callback(){

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                   populaTabela(database.celularDAO())
                }
            }
        }
        fun populaTabela(celularDao: CelularDao){
            celularDao.deleteAll()
            celularDao.insert(Celular(id= 1, modelo = "Teste 1", preco = 500.00))
            celularDao.insert(Celular(id= 2, modelo = "Teste 2", preco = 400.00))
            celularDao.insert(Celular(id= 3, modelo = "Teste 3", preco = 350.00))
        }
    }

}