package dk.arongk.and1_recipeapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dk.arongk.and1_recipeapp.models.ingredientListItem.IngredientListItemDto
import java.util.*

@Dao
interface IngredientListItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: IngredientListItemDto)

    @Query("SELECT * FROM ingredientListItems WHERE recipeId = :recipeId")
    fun getForRecipe(recipeId: UUID): LiveData<List<IngredientListItemDto>>

    @Query("SELECT * FROM ingredientListItems WHERE id = :id")
    fun get(id: UUID): LiveData<IngredientListItemDto>

    @Update
    suspend fun update(ingredientListItem: IngredientListItemDto)

    @Query("UPDATE ingredientListItems SET calories = :calories  WHERE recipeId = :recipeId")
    suspend fun updateCalories(recipeId: UUID, calories : String)

    @Delete
    suspend fun delete(ingredientListItem: IngredientListItemDto)
}