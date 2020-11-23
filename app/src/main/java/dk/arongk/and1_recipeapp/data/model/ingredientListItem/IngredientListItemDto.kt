package dk.arongk.and1_recipeapp.data.model.ingredientListItem

import androidx.room.*
import dk.arongk.and1_recipeapp.data.model.ingredient.IngredientDto
import dk.arongk.and1_recipeapp.data.model.recipe.RecipeDto
import java.util.*

@Entity(
    tableName = "ingredientListItems",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = RecipeDto::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("recipeId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = IngredientDto::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("ingredientId"),
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        )
    ),
    indices = arrayOf(Index(value = ["recipeId", "ingredientId"]))
)
data class IngredientListItemDto(
    @PrimaryKey()
    @ColumnInfo(name = "id")
    val id: UUID,
    @ColumnInfo(name = "recipeId")
    val recipeId: UUID,
    @ColumnInfo(name = "quantity")
    val quantity: Int,
    @ColumnInfo(name = "ingredientId")
    val ingredientId: UUID,
    @ColumnInfo(name = "ingredientName")
    val ingredientName: String,
    @ColumnInfo(name = "unit")
    val unit: String,
    @ColumnInfo(name = "operation")
    val operation: String
)