package dk.arongk.and1_recipeapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dk.arongk.and1_recipeapp.models.recipe.RecipeDto
import dk.arongk.and1_recipeapp.models.recipe.RecipeWithIngredientsDto
import java.util.*

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(recipe: RecipeDto)

    // wrap the complex model getters in transactions
    // see: https://developer.android.com/reference/android/arch/persistence/room/Transaction.html
    @Transaction
    @Query("SELECT * FROM recipes ORDER BY title DESC")
    fun getAll(): LiveData<List<RecipeWithIngredientsDto>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    fun get(id: UUID): LiveData<RecipeWithIngredientsDto>

    @Update
    suspend fun update(recipe: RecipeDto)

    @Delete
    suspend fun delete(recipe: RecipeDto)

}