package dk.arongk.and1_recipeapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dk.arongk.and1_recipeapp.data.RecipeDatabase
import dk.arongk.and1_recipeapp.data.repositories.IngredientListItemRepository
import dk.arongk.and1_recipeapp.models.recipe.RecipeDto
import dk.arongk.and1_recipeapp.data.repositories.RecipeRepository
import dk.arongk.and1_recipeapp.models.ingredientListItem.IngredientListItemDto
import java.util.*

class CurrentRecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val recipeRepository: RecipeRepository
    private val ingredientListItemRepository: IngredientListItemRepository
    var recipe: LiveData<RecipeDto>
    var ingredients: LiveData<List<IngredientListItemDto>>

    init {
        val db = RecipeDatabase.getDatabase(application, viewModelScope)
        recipeRepository = RecipeRepository(db.recipeDao(), db.ingredientListItemDao(), db.ingredientDao())
        ingredientListItemRepository = IngredientListItemRepository(db.ingredientListItemDao())

        recipe = MutableLiveData()
        ingredients = MutableLiveData()
    }

    fun updateRecipe(id : UUID){
        recipe = recipeRepository.get(id);
        ingredients = ingredientListItemRepository.getAllForRecipe(id)
    }

}