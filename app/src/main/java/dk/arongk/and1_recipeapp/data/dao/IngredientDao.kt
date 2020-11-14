package dk.arongk.and1_recipeapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dk.arongk.and1_recipeapp.data.model.IngredientDto

@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(ingredientDto: IngredientDto): Long

    @Query("SELECT * FROM ingredients ORDER BY name DESC")
     fun getAll(): LiveData<List<IngredientDto>>

    @Query("SELECT * FROM ingredients WHERE id = :id")
    suspend fun get(id: Int): IngredientDto

    @Update
    suspend fun update(ingredientDto: IngredientDto)

    @Delete
    suspend fun delete(ingredientDto: IngredientDto)
}