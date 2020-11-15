package dk.arongk.and1_recipeapp.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import dk.arongk.and1_recipeapp.R
import dk.arongk.and1_recipeapp.viewModels.CreateViewModel


class CreateFragment : Fragment(), View.OnClickListener {
    // CONSTANTS
    private val LOG_TAG = "CREATE_FRAGMENT"
    private val IMAGE_PICK_ACTIVITY_REQUEST_CODE = 1000;
    private val IMAGE_PICK_PERMISSION_REQUEST_CODE = 1001;

    // VARIABLES
    private lateinit var vm: CreateViewModel
    private lateinit var imageUri : String

    // WIDGETS
    private lateinit var title: TextInputEditText
    private lateinit var workTime: TextInputEditText
    private lateinit var totalTime: TextInputEditText
    private lateinit var servings: TextInputEditText
    private lateinit var description: TextInputEditText
    private lateinit var instructions: TextInputEditText
    private lateinit var notes: TextInputEditText
    private lateinit var recipeImage: ImageView
    private lateinit var createButton: Button
    private lateinit var addImageButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create, container, false)

        vm = ViewModelProvider(requireActivity()).get(CreateViewModel::class.java)

        initializeWidgets(view)

        imageUri = vm.imageUri

        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.createButton -> createRecipe()
            R.id.addImageButton -> pickImage(v)
        }
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    override fun onResume() {
        super.onResume()
        restoreData()
    }

    //TODO: allow to take new photo as well
    // image picker logic adapted from:
    // https://devofandroid.blogspot.com/2018/09/pick-image-from-gallery-android-studio_15.html
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            IMAGE_PICK_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this.context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_ACTIVITY_REQUEST_CODE) {
            recipeImage.setImageURI(data?.data)
            imageUri = data?.data.toString()
            vm.imageUri = imageUri
        }
    }

    private fun initializeWidgets(view: View) {
        title = view.findViewById(R.id.title)
        workTime = view.findViewById(R.id.workTime)
        totalTime = view.findViewById(R.id.totalTime)
        servings = view.findViewById(R.id.servings)
        description = view.findViewById(R.id.description)
        instructions = view.findViewById(R.id.instructions)
        notes = view.findViewById(R.id.notes)
        recipeImage = view.findViewById(R.id.recipeImage)
        createButton = view.findViewById(R.id.createButton)
        addImageButton = view.findViewById(R.id.addImageButton)

        createButton.setOnClickListener(this)
        addImageButton.setOnClickListener(this)
    }

    private fun createRecipe() {
        Log.i(LOG_TAG, "create recipe: " + title.text.toString())
        saveData()
        vm.insert()
        clearData()
    }

    private fun pickImage(v: View) {
        Toast.makeText(v.context, "Clicked on Image", Toast.LENGTH_LONG).show()
        //check runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                requestPermissions(permissions, IMAGE_PICK_PERMISSION_REQUEST_CODE);
            } else {
                pickImageFromGallery();
            }
        } else {
            //system OS is < Marshmallow
            pickImageFromGallery();
        }

    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_ACTIVITY_REQUEST_CODE)
    }

    private fun saveData() {
        vm.title = title.text.toString()
        vm.workTime = workTime.text.toString()
        vm.totalTime = totalTime.text.toString()
        vm.servings = servings.text.toString()
        vm.description = description.text.toString()
        vm.instructions = instructions.text.toString()
        vm.notes = notes.text.toString()
        vm.imageUri = imageUri
    }

    private fun restoreData() {
        title.setText(vm.title)
        workTime.setText(vm.workTime)
        totalTime.setText(vm.totalTime)
        servings.setText(vm.servings)
        description.setText(vm.description)
        instructions.setText(vm.workTime)
        notes.setText(vm.notes)
        recipeImage.setImageURI(Uri.parse(vm.imageUri))
        imageUri = vm.imageUri
    }

    private fun clearData() {
        vm.title = ""
        vm.workTime = ""
        vm.totalTime = ""
        vm.servings = ""
        vm.description = ""
        vm.instructions = ""
        vm.notes = ""
        vm.imageUri = ""
        restoreData()
    }

}