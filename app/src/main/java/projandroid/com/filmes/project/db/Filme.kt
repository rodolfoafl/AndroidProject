package projandroid.com.filmes.project.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "filmes")
data class Filme (

        @ColumnInfo(name = "nome")
        var nome: String,
        @ColumnInfo(name = "descricao")
        var descricao: String
):Serializable {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long = 0
}