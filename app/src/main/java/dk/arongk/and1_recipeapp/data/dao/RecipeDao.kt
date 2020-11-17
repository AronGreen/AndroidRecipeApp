package dk.arongk.and1_recipeapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dk.arongk.and1_recipeapp.data.model.recipe.RecipeDto
import java.util.*

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(recipe: RecipeDto)

    @Query("SELECT * FROM recipes ORDER BY title DESC")
    fun getAll(): LiveData<List<RecipeDto>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun get(id: UUID): LiveData<RecipeDto>

    @Update
    suspend fun update(recipe: RecipeDto)

    @Delete
    suspend fun delete(recipe: RecipeDto)

}