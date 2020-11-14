package dk.arongk.and1_recipeapp.data.model.tag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tags")
class TagDto(
    @PrimaryKey()
    @ColumnInfo(name = "id")
    val id: UUID,
    @ColumnInfo(name = "value")
    val value: String
) {
}