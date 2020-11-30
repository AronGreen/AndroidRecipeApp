package dk.arongk.and1_recipeapp.data.repositories

import dk.arongk.and1_recipeapp.CoroutineTestRule
import dk.arongk.and1_recipeapp.data.dao.IngredientDao
import dk.arongk.and1_recipeapp.data.dao.IngredientListItemDao
import dk.arongk.and1_recipeapp.data.dao.RecipeDao
import dk.arongk.and1_recipeapp.models.recipe.RecipeDto
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.util.*

@ExperimentalCoroutinesApi
class RecipeRepositoryTest : TestCase() {
    // NOTE: all tests will say that recipeDao.getAll() has been called because it is called when
    // val allRecipes is initialized - maybe this should be changed to a method in stead

    // this makes it possible to test suspending functions
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Mock
    private val mockRecipeDao: RecipeDao= Mockito.mock(RecipeDao::class.java)

    @Mock
    private val  mockIngredientListItemDao: IngredientListItemDao= Mockito.mock(IngredientListItemDao::class.java)

    @Mock
    private val mockIngredientDao: IngredientDao = Mockito.mock(IngredientDao::class.java)

    private val sut: RecipeRepository= RecipeRepository(
        mockRecipeDao,
        mockIngredientListItemDao,
        mockIngredientDao
    ).also { it.dispatchers = coroutinesTestRule.testDispatcherProvider }

    @Test
    fun testGet() {
        val recipeId = UUID.randomUUID()
        sut.get(recipeId)
        Mockito.verify(mockRecipeDao).get(recipeId)
    }

    @Test
    fun testGetAllRecipes() {
        sut.allRecipes
        Mockito.verify(mockRecipeDao).getAll()
    }

    @Test
    fun testUpdate() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val mockUpdateModel = RecipeDto(UUID.randomUUID(), "", 1, 2, 3, "", "", "")
        sut.update(mockUpdateModel)
        Mockito.verify(mockRecipeDao).update(mockUpdateModel)
        Mockito.verify(mockRecipeDao).getAll()
    }

    @Test
    fun testDelete() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val mockUpdateModel = RecipeDto(UUID.randomUUID(), "", 1, 2, 3, "", "", "")
        sut.delete(mockUpdateModel)
        Mockito.verify(mockRecipeDao).delete(mockUpdateModel)
    }

    @Test
    fun testInsert() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val mockUpdateModel = RecipeDto(UUID.randomUUID(), "", 1, 2, 3, "", "", "")
        sut.update(mockUpdateModel)
        Mockito.verify(mockRecipeDao).update(mockUpdateModel)
    }
}