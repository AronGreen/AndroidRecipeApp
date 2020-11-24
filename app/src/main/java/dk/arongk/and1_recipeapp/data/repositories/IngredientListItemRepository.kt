package dk.arongk.and1_recipeapp.data.repositories

import androidx.lifecycle.LiveData
import dk.arongk.and1_recipeapp.data.dao.IngredientListItemDao
import dk.arongk.and1_recipeapp.models.ingredientListItem.IngredientListItemDto
import java.util.*

class IngredientListItemRepository(
    private val ingredientListItemDao: IngredientListItemDao
) {

    fun get(id: UUID): LiveData<IngredientListItemDto> {
        return ingredientListItemDao.get(id)
    }

    fun getAllForRecipe(recipeId: UUID): LiveData<List<IngredientListItemDto>> {
        return ingredientListItemDao.getForRecipe(recipeId)
    }
}