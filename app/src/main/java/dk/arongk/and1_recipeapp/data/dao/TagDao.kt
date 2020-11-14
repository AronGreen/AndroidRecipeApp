package dk.arongk.and1_recipeapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dk.arongk.and1_recipeapp.data.model.TagDto
import java.util.*

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(tag: TagDto)

    @Query("SELECT * FROM tags ORDER BY value DESC")
    fun getAll(): LiveData<List<TagDto>>

    @Query("SELECT * FROM tags WHERE id = :id")
    suspend fun get(id: UUID): TagDto

    @Update
    suspend fun update(tag: TagDto)

    @Delete
    suspend fun delete(tag: TagDto)

    @Query("SELECT * FROM tags WHERE value = :value")
    fun deleteByValue(value: String) : Long
}