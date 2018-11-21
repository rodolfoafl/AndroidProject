package projandroid.com.produtos.project.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "celulares")
data class Celular (
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long,
        @ColumnInfo(name = "modelo")
        var modelo: String,
        @ColumnInfo(name = "preco")
        var preco: Double
)