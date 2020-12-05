package dk.arongk.and1_recipeapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dk.arongk.and1_recipeapp.data.dao.IngredientDao
import dk.arongk.and1_recipeapp.data.dao.IngredientListItemDao
import dk.arongk.and1_recipeapp.data.dao.RecipeDao
import dk.arongk.and1_recipeapp.data.dao.TagDao
import dk.arongk.and1_recipeapp.models.ingredient.IngredientDto
import dk.arongk.and1_recipeapp.models.ingredientListItem.IngredientListItemDto
import dk.arongk.and1_recipeapp.models.recipe.RecipeDto
import dk.arongk.and1_recipeapp.models.tag.TagDto
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = arrayOf(
        RecipeDto::class,
        IngredientListItemDto::class,
        IngredientDto::class,
        TagDto::class
    ), version = 1, exportSchema = false
) // TODO: look into exportSchema
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun ingredientListItemDao(): IngredientListItemDao
    abstract fun tagDao(): TagDao

    private class RecipeDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        //NOTE: Uncomment this and modify if database population is necessary
//        override fun onOpen(db: SupportSQLiteDatabase) {
//            super.onOpen(db)
//            INSTANCE?.let { database ->
//                scope.launch {
//                    populateDatabase(database.recipeDao())
//                }
//            }
//        }

//        suspend fun populateDatabase(recipeDao: RecipeDao) {
//            val createRecipeModel = RecipeCreateModel().apply {
//                title = "Mashed potatoes"
//                workTime = 20
//                totalTime = 40
//                servings = 2
//            }
//            recipeDao.insert(createRecipeModel.toDto())
//        }
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
                    "recipe_database"
                )
                    .addCallback(RecipeDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}