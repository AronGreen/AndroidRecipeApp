package dk.arongk.and1_recipeapp.data.repositories

import androidx.lifecycle.LiveData
import dk.arongk.and1_recipeapp.data.dao.IngredientListItemDao
import dk.arongk.and1_recipeapp.data.dao.RecipeDao
import dk.arongk.and1_recipeapp.data.model.recipe.RecipeCreateModel
import dk.arongk.and1_recipeapp.data.model.recipe.RecipeDto
import java.util.*

class RecipeRepository(
    private val recipeDao: RecipeDao,
    private val ingredientListItemDao: IngredientListItemDao
) {

    suspend fun get(id: UUID): RecipeDto {
        return recipeDao.get(id)
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    fun getAll(): LiveData<List<RecipeDto>> {
        return recipeDao.getAll()
    }

    val allRecipes : LiveData<List<RecipeDto>> = recipeDao.getAll()


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

        val ingredients = recipe.ingredients
        for (ingredient in ingredients) {
            ingredientListItemDao.insert(ingredient.copy(recipeId = newId))
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

    suspend fun getRecipeFull(id: UUID): RecipeDto? {
        val recipe = recipeDao.get(id)
        recipe.ingredients = ingredientListItemDao.getForRecipe(id)
        return recipe
    }
}