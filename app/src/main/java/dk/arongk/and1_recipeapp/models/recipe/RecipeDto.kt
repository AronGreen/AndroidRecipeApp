package dk.arongk.and1_recipeapp.models.recipe

import androidx.room.*
import dk.arongk.and1_recipeapp.models.ingredientListItem.IngredientListItemDto
import java.util.*

@Entity(
    tableName = "recipes",
    indices = [Index(value = ["id"])]
)
data class RecipeDto(
    @PrimaryKey()
    @ColumnInfo(name = "id")
    val id: UUID,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "workTime")
    val workTime: Int,
    @ColumnInfo(name = "totalTime")
    val totalTime: Int,
    @ColumnInfo(name = "servings")
    val servings: Int,
    @ColumnInfo(name = "instructions")
    val instructions: String,
    @ColumnInfo(name = "notes")
    val notes: String,
    @ColumnInfo(name = "image")
    val imageUrl: String,
) {
    @Ignore
    var ingredients: List<IngredientListItemDto>? = null
}