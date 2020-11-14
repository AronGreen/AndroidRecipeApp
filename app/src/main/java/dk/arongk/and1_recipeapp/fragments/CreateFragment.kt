package dk.arongk.and1_recipeapp.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import dk.arongk.and1_recipeapp.R
import dk.arongk.and1_recipeapp.viewModels.CreateFragmentViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val LOG_TAG = "CREATE_FRAGMENT"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateFragment : Fragment(), View.OnClickListener {
    private lateinit var createFragmentViewModel: CreateFragmentViewModel

    private lateinit var createButton : Button

    private lateinit var title : TextInputEditText
    private lateinit var workTime : TextInputEditText
    private lateinit var totalTime : TextInputEditText
    private lateinit var servings : TextInputEditText
    private lateinit var description : TextInputEditText
    private lateinit var instructions : TextInputEditText
    private lateinit var notes : TextInputEditText
    private lateinit var imageUrl : TextInputEditText


    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }

        createFragmentViewModel = ViewModelProvider(this).get(CreateFragmentViewModel::class.java)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i(LOG_TAG,"onViewCreated!?")
        title = view.findViewById(R.id.title)
        workTime = view.findViewById(R.id.workTime)
        totalTime = view.findViewById(R.id.totalTime)
        servings = view.findViewById(R.id.servings)
        description = view.findViewById(R.id.description)
        instructions = view.findViewById(R.id.instructions)
        notes = view.findViewById(R.id.notes)
        imageUrl = view.findViewById(R.id.imageUrl)


        title.setText("yo")
//        createButton = view.findViewById(R.id.createButton)
//        createButton.setOnClickListener(View.OnClickListener {
//            @Override
//            fun onClick(view : View){
//                createFragmentViewModel.createModel.title = title.text.toString()
//                createFragmentViewModel.createModel.workTime = workTime.text.toString().toIntOrNull() ?: 0
//                createFragmentViewModel.createModel.totalTime = totalTime.text.toString().toIntOrNull() ?:0
//                createFragmentViewModel.createModel.servings = servings.text.toString().toIntOrNull() ?:0
//                createFragmentViewModel.createModel.description = description.text.toString()
//                createFragmentViewModel.createModel.instructions = instructions.text.toString()
//                createFragmentViewModel.createModel.notes = notes.text.toString()
//                createFragmentViewModel.createModel.imageUrl = imageUrl.text.toString()
//                createFragmentViewModel.insert(createFragmentViewModel.createModel)
//            }
//        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(LOG_TAG,"onCreateView!?")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create, container, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateFragment().apply {
                Log.i(LOG_TAG,"newInstance!?")

                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(v: View?) {

        Log.i(LOG_TAG,"click!?")
        when(v!!.id){
            R.id.createButton -> createFun()
        }
    }

    fun createFun(){
        Log.i(LOG_TAG,"create!?")
        createFragmentViewModel.createModel.title = title.text.toString()
        createFragmentViewModel.createModel.workTime = workTime.text.toString().toIntOrNull() ?: 0
        createFragmentViewModel.createModel.totalTime = totalTime.text.toString().toIntOrNull() ?:0
        createFragmentViewModel.createModel.servings = servings.text.toString().toIntOrNull() ?:0
        createFragmentViewModel.createModel.description = description.text.toString()
        createFragmentViewModel.createModel.instructions = instructions.text.toString()
        createFragmentViewModel.createModel.notes = notes.text.toString()
        createFragmentViewModel.createModel.imageUrl = imageUrl.text.toString()
        createFragmentViewModel.insert(createFragmentViewModel.createModel)
    }
}