package dk.arongk.and1_recipeapp.models.ingredient

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "ingredients")
data class IngredientDto(
    @PrimaryKey()
    @ColumnInfo(name = "id")
    val id : UUID,
    @ColumnInfo(name = "name")
    val name : String,
    // TODO: nutritional info
)