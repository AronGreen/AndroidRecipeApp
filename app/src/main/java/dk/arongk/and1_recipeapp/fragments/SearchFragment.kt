package dk.arongk.and1_recipeapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.arongk.and1_recipeapp.R
import dk.arongk.and1_recipeapp.adapters.RecipeAdapter
import dk.arongk.and1_recipeapp.viewModels.SearchViewModel

class SearchFragment : Fragment(), RecipeAdapter.OnListItemClickListener {

    // PROPS
    private lateinit var vm: SearchViewModel

    // WIDGETS
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        vm = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)

        initializeWidgets(view, vm)

        return view;
    }


    private fun initializeWidgets(view: View, vm : SearchViewModel) {
        recyclerView = view.findViewById(R.id.search_recyclerView)

        val adapter = RecipeAdapter(requireContext(), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        vm.recipes.observe(requireActivity(), Observer {recipes ->
            // Update the cached copy of the words in the adapter.
            recipes?.let { adapter.setData(it) }
        })

        vm.updateRecipes()

        recyclerView.hasFixedSize()
    }

    override fun onListItemClick(clickedItemIndex: Int) {
        val idString = vm.recipes.value?.get(clickedItemIndex)?.id.toString()
        setCurrentRecipeId(idString)
        findNavController().navigate(R.id.action_searchFragment_to_currentRecipeFragment)
    }

    private fun setCurrentRecipeId(idString : String){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(getString(R.string.current_recipe_id_string), idString)
            apply()
        }
    }

}