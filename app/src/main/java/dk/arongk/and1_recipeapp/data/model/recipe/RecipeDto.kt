package dk.arongk.and1_recipeapp.data.model.recipe

import androidx.room.*
import dk.arongk.and1_recipeapp.data.model.ingredientListItem.IngredientListItemDto
import dk.arongk.and1_recipeapp.data.model.tag.TagDto
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
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "instructions")
    val instructions: String,
    @ColumnInfo(name = "notes")
    val notes: String,
    @ColumnInfo(name = "image")
    val imageUrl: String,
    ) {
    @Ignore
    var ingredients: List<IngredientListItemDto>? = null
    @Ignore
    var tags: List<TagDto>? = null
}