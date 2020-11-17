package dk.arongk.and1_recipeapp.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dk.arongk.and1_recipeapp.R
import dk.arongk.and1_recipeapp.data.model.recipe.RecipeDto
import dk.arongk.and1_recipeapp.viewModels.CurrentRecipeViewModel
import java.util.*

//private const val ARG_RECIPE_ID = "recipeId"

class CurrentRecipeFragment : Fragment() {

    // PROPS
//    private var recipeId: UUID? = null
    private lateinit var vm: CurrentRecipeViewModel

    // WIDGETS
    private lateinit var image: ImageView
    private lateinit var title: TextView


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            recipeId = UUID.fromString(it.getString(ARG_RECIPE_ID))
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_current_recipe, container, false)

//        savedInstanceState?.getString("recipeId")?.let {
//            recipeId = UUID.fromString(it)
//        }

        vm = ViewModelProvider(requireActivity()).get(CurrentRecipeViewModel::class.java)

        initializeWidgets(view, vm)

        return view
    }

    private fun initializeWidgets(view: View, vm: CurrentRecipeViewModel) {
//        recipeId?.let { vm.updateRecipe(it) }

        getRecipeId().apply {
            this?.let { vm.updateRecipe(it) }
            Log.d("getRecipeId", vm.recipe.value.toString())
        }

        vm.recipe.observe(requireActivity(), androidx.lifecycle.Observer {
            it?.let { updateWidgets(it) }
        })
        image = view.findViewById(R.id.current_image)

        title = view.findViewById(R.id.current_title)

//        updateWidgets(it)
    }

    private fun updateWidgets(recipe: RecipeDto) {
        vm.recipe.value?.let {
            image.setImageURI(Uri.parse(it.imageUrl))
            title.text = it.title
        }
        Log.d("getRecipeId", vm.recipe.value.toString())
    }

    private fun getRecipeId(): UUID? {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val idString = sharedPref.getString(getString(R.string.current_recipe_id_string), "")
        Log.d("getRecipeId", idString ?: "none")
        return UUID.fromString(idString)
    }

//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param recipeId Id of recipe to show.
//         * @return A new instance of fragment CurrentRecipeFragment.
//         */
//        @JvmStatic
//        fun newInstance(recipeId: String) =
//            CurrentRecipeFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_RECIPE_ID, recipeId)
//                }
//            }
//    }
}