package dk.arongk.and1_recipeapp.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dk.arongk.and1_recipeapp.R
import dk.arongk.and1_recipeapp.models.ingredientListItem.IngredientListItemCreateModel
import dk.arongk.and1_recipeapp.models.ingredientListItem.IngredientListItemDto
import dk.arongk.and1_recipeapp.viewModels.CurrentRecipeViewModel
import org.w3c.dom.Text
import java.util.*

private const val LOG_TAG = "CurrentRecipeFragment"

class CurrentRecipeFragment : Fragment() {
    // PROPS
    private lateinit var vm: CurrentRecipeViewModel

    // WIDGETS
    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var ingredientsTable: TableLayout
    private lateinit var workTime : TextView
    private lateinit var totalTime : TextView
    private lateinit var servings : TextView
    private lateinit var instructions : TextView
    private lateinit var notes : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_current_recipe, container, false)

        vm = ViewModelProvider(this).get(CurrentRecipeViewModel::class.java)

        initializeWidgets(view, vm)

        return view
    }

    private fun initializeWidgets(view: View, vm: CurrentRecipeViewModel) {
        getRecipeId().apply {
            // TODO: should redirect to either create or search if no recipe is currently selected
            // Or the navigation button should be disabled if no recipe is currently selected
            this?.let { vm.updateRecipe(it) }
            Log.d("getRecipeId", vm.recipe.value.toString())
        }

        vm.recipe.observe(requireActivity(), {
            it?.let { updateWidgets() }
        })
        vm.ingredients.observe(requireActivity(), {
            it?.let { updateIngredients() }
        })

        image = view.findViewById(R.id.current_image)
        title = view.findViewById(R.id.current_title)
        ingredientsTable = view.findViewById(R.id.current_ingredientsTable)
        workTime = view.findViewById(R.id.current_workTime)
        totalTime = view.findViewById(R.id.current_totalTime)
        servings = view.findViewById(R.id.current_servings)
        instructions = view.findViewById(R.id.current_instructions)
        notes = view.findViewById(R.id.current_notes)
    }

    private fun updateIngredients() {
        vm.ingredients.value?.let {
            ingredientsTable.removeAllViews()
            vm.ingredients.value?.forEach {
                addIngredientListItem(it)
            }
        }
    }

    private fun updateWidgets() {
        vm.recipe.value?.let {
            image.setImageURI(Uri.parse(it.imageUrl))
            title.text = it.title
            workTime.text = it.workTime.toString()
            totalTime.text = it.totalTime.toString()
            servings.text = it.servings.toString()
            instructions.text = it.instructions
            notes.text = it.notes
        }
        Log.d("getRecipeId", vm.recipe.value.toString())
    }

    private fun getRecipeId(): UUID? {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val idString = sharedPref.getString(getString(R.string.current_recipe_id_string), null)
        Log.d(LOG_TAG, "getRecipeId(): $idString")
        return if (idString != null) UUID.fromString(idString) else null
    }

    private fun addIngredientListItem(ingredient: IngredientListItemDto) {
        val ingredientTR = TableRow(requireContext())
        ingredientTR.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT
        )

        val ingredientName = TextView(requireContext())
        ingredientName.text = ingredient.ingredientName.toString()
        ingredientName.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT,
        )
        ingredientName.setPadding(
            0,
            0,
            16,
            0
        )

        val quantity = TextView(requireContext())
        quantity.text = ingredient.quantity.toString()
        quantity.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT,
        )
        quantity.gravity = Gravity.END
        quantity.setPadding(
            0,
            0,
            16,
            0
        )

        val unit = TextView(requireContext())
        unit.text = ingredient.unit.toString()
        unit.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT,
        )
        unit.setPadding(
            0,
            0,
            16,
            0
        )

        val operation = TextView(requireContext())
        operation.text = ingredient.operation.toString()
        operation.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT,
        )

        ingredientTR.addView(quantity)
        ingredientTR.addView(unit)
        ingredientTR.addView(ingredientName)
        ingredientTR.addView(operation)

        ingredientsTable.addView(ingredientTR)
    }

}