package dk.arongk.and1_recipeapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import dk.arongk.and1_recipeapp.data.dao.IngredientDao
import dk.arongk.and1_recipeapp.data.dao.IngredientListItemDao
import dk.arongk.and1_recipeapp.data.model.recipe.RecipeDto
import dk.arongk.and1_recipeapp.data.dao.RecipeDao
import dk.arongk.and1_recipeapp.data.dao.TagDao
import dk.arongk.and1_recipeapp.data.model.IngredientDto
import dk.arongk.and1_recipeapp.data.model.IngredientListItemDto
import dk.arongk.and1_recipeapp.data.model.TagDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = arrayOf(
    RecipeDto::class,
    IngredientListItemDto::class,
    IngredientDto::class,
    TagDto::class
), version = 1, exportSchema = false) // TODO: look into exportSchema
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun ingredientListItemDao(): IngredientListItemDao
    abstract fun tagDao(): TagDao

    private class RecipeDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.tagDao())
                }
            }
        }

        suspend fun populateDatabase(tagDao: TagDao) {
            tagDao.deleteByValue("test")
            tagDao.insert(TagDto(UUID.randomUUID(),"test"))
        }
    }


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): RecipeDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database")
                    .addCallback(RecipeDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}