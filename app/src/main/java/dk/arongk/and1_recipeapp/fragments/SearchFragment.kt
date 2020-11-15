package dk.arongk.and1_recipeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.arongk.and1_recipeapp.R
import dk.arongk.and1_recipeapp.adapters.RecipeAdapter
import dk.arongk.and1_recipeapp.viewModels.SearchViewModel

class SearchFragment : Fragment() {

    // CONSTANTS
    private val LOG_TAG = "SEARCH_FRAGMENT"

    // VARIABLES
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
        initializeWidgets(view)
        val adapter = RecipeAdapter(vm.recipes.value ?: emptyList())
        recyclerView.adapter = adapter

        vm.recipes.observe(requireActivity(), Observer {recipes ->
            // Update the cached copy of the words in the adapter.
            recipes?.let { adapter.setData(it) }
        })

        return view;
    }


    private fun initializeWidgets(view: View) {
        recyclerView = view.findViewById(R.id.search_recyclerView)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.visibility = View.INVISIBLE
    }

}