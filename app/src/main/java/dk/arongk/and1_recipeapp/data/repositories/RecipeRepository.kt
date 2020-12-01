package dk.arongk.and1_recipeapp.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import dk.arongk.and1_recipeapp.data.apiClients.NutritionService
import dk.arongk.and1_recipeapp.data.apiClients.NutritionAnalysisResponse
import dk.arongk.and1_recipeapp.data.apiClients.NutritionServiceBuilder
import dk.arongk.and1_recipeapp.data.dao.IngredientDao
import dk.arongk.and1_recipeapp.data.dao.IngredientListItemDao
import dk.arongk.and1_recipeapp.data.dao.RecipeDao
import dk.arongk.and1_recipeapp.models.ingredient.IngredientDto
import dk.arongk.and1_recipeapp.models.ingredientListItem.IngredientListItemCreateModel
import dk.arongk.and1_recipeapp.models.recipe.RecipeCreateModel
import dk.arongk.and1_recipeapp.models.recipe.RecipeDto
import dk.arongk.and1_recipeapp.models.recipe.RecipeWithIngredientsDto
import dk.arongk.and1_recipeapp.util.DefaultDispatcherProvider
import dk.arongk.and1_recipeapp.util.DispatcherProvider
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

    var dispatchers: DispatcherProvider = DefaultDispatcherProvider()

    private val analysisEndpoints: NutritionService =
        NutritionServiceBuilder.buildService(NutritionService::class.java)

    fun get(id: UUID): LiveData<RecipeWithIngredientsDto> {
        return recipeDao.get(id)
    }

    val allRecipes: LiveData<List<RecipeWithIngredientsDto>> = recipeDao.getAll()

    //TODO: use updateRecipeModel
    suspend fun update(recipeDto: RecipeDto) {
        recipeDao.update(recipeDto)
    }

    suspend fun delete(recipeDto: RecipeDto) {
        recipeDao.delete(recipeDto)
    }

    suspend fun insert(recipe: RecipeCreateModel): UUID {
        recipeDao.insert(recipe.toDto())

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

                ingredientListItemDao.insert(it.toDto(recipe.id, ingredientId))
                getNutritionAnalysis(it)
            }

        return recipe.id
    }

    private suspend fun getNutritionAnalysis(
        createModel: IngredientListItemCreateModel
    ) {

        val call = analysisEndpoints.getNutritionAnalysis(createModel.toApiRequestString())

        call.enqueue(object : Callback<NutritionAnalysisResponse> {
            override fun onResponse(
                call: Call<NutritionAnalysisResponse>,
                response: Response<NutritionAnalysisResponse>
            ) {
                Log.d(LOG_TAG, response.message())
                if (response.isSuccessful) {
                    GlobalScope.launch(dispatchers.io()) {
                        ingredientListItemDao.updateCalories(
                            createModel.id,
                            response.body()?.calories?.toString() ?: ""
                        )
                    }
                }

            }

            override fun onFailure(call: Call<NutritionAnalysisResponse>, t: Throwable) {
                Log.d(LOG_TAG, t.message ?: t.toString())
                // TODO: save failed ingredients for later retry
            }
        })
    }

    companion object{
        private const val LOG_TAG = "RecipeRepository"
    }
}