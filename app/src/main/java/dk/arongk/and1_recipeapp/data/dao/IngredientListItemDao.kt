package dk.arongk.and1_recipeapp.data.dao

import androidx.room.*
import dk.arongk.and1_recipeapp.data.model.IngredientListItemDto
import java.util.*

@Dao
interface IngredientListItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: IngredientListItemDto)

    @Query("SELECT * FROM ingredientListItems WHERE recipeId = :recipeId")
    suspend fun getForRecipe(recipeId: UUID): List<IngredientListItemDto>

    @Query("SELECT * FROM ingredientListItems WHERE id = :id")
    suspend fun get(id: UUID): IngredientListItemDto

    @Update
    suspend fun update(ingredientListItem: IngredientListItemDto)

    @Delete
    suspend fun delete(ingredientListItem: IngredientListItemDto)
}