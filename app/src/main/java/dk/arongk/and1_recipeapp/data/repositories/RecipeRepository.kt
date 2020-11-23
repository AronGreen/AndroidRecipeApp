package dk.arongk.and1_recipeapp.data.repositories

import androidx.lifecycle.LiveData
import dk.arongk.and1_recipeapp.data.dao.IngredientDao
import dk.arongk.and1_recipeapp.data.dao.IngredientListItemDao
import dk.arongk.and1_recipeapp.data.dao.RecipeDao
import dk.arongk.and1_recipeapp.models.ingredient.IngredientDto
import dk.arongk.and1_recipeapp.models.recipe.RecipeCreateModel
import dk.arongk.and1_recipeapp.models.recipe.RecipeDto
import java.util.*

class RecipeRepository(
    private val recipeDao: RecipeDao,
    private val ingredientListItemDao: IngredientListItemDao,
    private val ingredientDao: IngredientDao
) {

    fun get(id: UUID): LiveData<RecipeDto> {
        return recipeDao.get(id)
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    fun getAll(): LiveData<List<RecipeDto>> {
        return recipeDao.getAll()
    }

    val allRecipes: LiveData<List<RecipeDto>> = recipeDao.getAll()


    //TODO: use updateRecipeModel
    suspend fun update(recipeDto: RecipeDto) {
        recipeDao.update(recipeDto)
    }

    suspend fun delete(recipeDto: RecipeDto) {
        recipeDao.delete(recipeDto)
    }

    suspend fun insert(recipe: RecipeCreateModel): UUID {
        val newId = UUID.randomUUID()

        recipeDao.insert(recipe.toDto(newId))


        recipe.ingredients
//            .filter {
//                it.ingredientName.isNotBlank()
//            }
            .forEach {
                // TODO: this could probs be more concise, too tired rn
                val existingIngredient = ingredientDao.get(it.ingredientName)
                val ingredientId: UUID = existingIngredient?.id ?: UUID.randomUUID()
                if (existingIngredient == null) {
                    ingredientDao.insert(IngredientDto(ingredientId, it.ingredientName))
                }

                ingredientListItemDao.insert(it.toDto(UUID.randomUUID(), newId, ingredientId))
            }


//        val tags = recipe.tags
//        if (tags != null){
//            for (tag in tags){
//                // TODO: tagDao
//            }
//
//        }

        return newId
    }

//    suspend fun getRecipeFull(id: UUID): RecipeDto? {
//        val recipe = recipeDao.get(id)
//        recipe.ingredients = ingredientListItemDao.getForRecipe(id)
//        return recipe
//    }
}