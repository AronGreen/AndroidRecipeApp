package dk.arongk.and1_recipeapp.data.repositories

import dk.arongk.and1_recipeapp.data.dao.IngredientListItemDao
import junit.framework.TestCase
import org.mockito.Mock
import org.mockito.Mockito
import java.util.*


class IngredientListItemRepositoryTest : TestCase() {

    @Mock
    private val mockIngredientListItemDao: IngredientListItemDao =
        Mockito.mock(IngredientListItemDao::class.java)

    private val sut: IngredientListItemRepository =
        IngredientListItemRepository(mockIngredientListItemDao)

    fun testGet() {
        val id = UUID.randomUUID()
        sut.get(id)
        Mockito.verify(mockIngredientListItemDao).get(id)
    }

    fun testGetAllForRecipe() {
        val recipeId = UUID.randomUUID()
        sut.getAllForRecipe(recipeId)
        Mockito.verify(mockIngredientListItemDao).getForRecipe(recipeId)
    }
}