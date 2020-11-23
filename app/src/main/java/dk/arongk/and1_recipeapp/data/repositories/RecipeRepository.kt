package dk.arongk.and1_recipeapp.data.repositories

import androidx.lifecycle.LiveData
import dk.arongk.and1_recipeapp.data.apiClients.NutritionAnalysisEndpoints
import dk.arongk.and1_recipeapp.data.apiClients.NutritionAnalysisResponse
import dk.arongk.and1_recipeapp.data.apiClients.ServiceBuilder
import dk.arongk.and1_recipeapp.data.dao.IngredientDao
import dk.arongk.and1_recipeapp.data.dao.IngredientListItemDao
import dk.arongk.and1_recipeapp.data.dao.RecipeDao
import dk.arongk.and1_recipeapp.models.ingredient.IngredientDto
import dk.arongk.and1_recipeapp.models.ingredientListItem.IngredientListItemCreateModel
import dk.arongk.and1_recipeapp.models.recipe.RecipeCreateModel
import dk.arongk.and1_recipeapp.models.recipe.RecipeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
            .filter {
                it.ingredientName.isNotBlank()
            }
            .forEach {
                // TODO: this could probs be more concise, too tired rn
                val existingIngredient = ingredientDao.get(it.ingredientName)
                val ingredientId: UUID = existingIngredient?.id ?: UUID.randomUUID()
                if (existingIngredient == null) {
                    ingredientDao.insert(IngredientDto(ingredientId, it.ingredientName))
                }

                ingredientListItemDao.insert(it.toDto(UUID.randomUUID(), newId, ingredientId))
                getNutritionAnalysis(it, newId)
            }

        return newId
    }

    private suspend fun getNutritionAnalysis(createModel: IngredientListItemCreateModel, dtoId : UUID ){

        val request = ServiceBuilder.buildService(NutritionAnalysisEndpoints::class.java)
        val call = request.getNutritionAnalysis(createModel.toString())

        call.enqueue(object : Callback<NutritionAnalysisResponse> {
            override fun onResponse(call: Call<NutritionAnalysisResponse>, response: Response<NutritionAnalysisResponse>) {
                if (response.isSuccessful){
                    GlobalScope.launch(Dispatchers.IO) {

                        ingredientListItemDao.updateCalories(dtoId, response.body()?.calories?.toString() ?: "")

                    }
                }
            }
            override fun onFailure(call: Call<NutritionAnalysisResponse>, t: Throwable) {
                // TODO: save failed ingredients for later retry
            }
        })
    }

}