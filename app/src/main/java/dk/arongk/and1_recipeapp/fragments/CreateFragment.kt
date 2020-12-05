package dk.arongk.and1_recipeapp.fragments

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dk.arongk.and1_recipeapp.R
import dk.arongk.and1_recipeapp.models.ingredientListItem.IngredientListItemCreateModel
import dk.arongk.and1_recipeapp.models.recipe.RecipeCreateModel
import dk.arongk.and1_recipeapp.viewModels.CreateViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// some code adapted from:
// https://devofandroid.blogspot.com/2018/09/pick-image-from-gallery-android-studio_15.html
// http://www.kotlincodes.com/kotlin/camera-intent-with-kotlin-android/
class CreateFragment : Fragment(), View.OnClickListener {
    // PROPS
    private lateinit var vm: CreateViewModel
    private lateinit var imageUri: String
    private lateinit var currentPhotoPath: String

    // keeping track of programatically added EditText elements
    private data class IngredientValues(
        val qty: EditText,
        val unit: Spinner,
        val name: EditText,
        val operation: EditText
    )

    private val ingredientValues: MutableList<IngredientValues> = mutableListOf()

    // WIDGETS
    private lateinit var title: TextInputEditText
    private lateinit var workTime: TextInputEditText
    private lateinit var totalTime: TextInputEditText
    private lateinit var servings: TextInputEditText
    private lateinit var instructions: TextInputEditText
    private lateinit var notes: TextInputEditText
    private lateinit var recipeImage: ImageView
    private lateinit var createButton: Button
    private lateinit var addImageButton: Button
    private lateinit var addIngredientButton: Button
    private lateinit var ingredientsLinearLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create, container, false)
        vm = ViewModelProvider(requireActivity()).get(CreateViewModel::class.java)
        imageUri = vm.imageUri
        initializeWidgets(view)
        initializeIngredientList()
        return view
    }

    override fun onPause() {
        super.onPause()
        saveToViewModel()
    }

    override fun onResume() {
        super.onResume()
        restoreFromViewModel()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.createButton -> createRecipe()
            R.id.addImageButton -> initTakePicture()
            R.id.addIngredientButton -> addIngredient()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (allPermissionsGranted(grantResults)) {
                    takePicture()
                    // TODO: or pick image from gallery
                } else {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    recipeImage.visibility = View.VISIBLE
                    addImageButton.text = getString(R.string.change_image)
                    val bitmap: Bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                    recipeImage.setImageBitmap(bitmap)
                    vm.imageUri = currentPhotoPath
                }
                // TODO: pick from gallery
            }
        // TODO: handle reject
    }

    private fun createRecipe() {
        saveToViewModel()

        val model = RecipeCreateModel()
        model.title = vm.title
        model.workTime = vm.workTime.toIntOrNull() ?: 0
        model.totalTime = vm.totalTime.toIntOrNull() ?: 0
        model.servings = vm.servings.toIntOrNull() ?: 0
        model.instructions = vm.instructions
        model.notes = vm.notes
        model.imageUrl = vm.imageUri
        // pass a copy, otherwise it will be cleared before we can dao anything with it :)
        model.ingredients = vm.ingredients.toMutableList()

        vm.insert(model = model, findNavController(), requireActivity(), this)
        clearViewModel()
    }

    private fun initTakePicture() =
        if (checkPermission()) takePicture() else requestPermission()

    private fun allPermissionsGranted(grantResults: IntArray): Boolean {
        return grantResults.isNotEmpty() && grantResults.all { x -> x == PackageManager.PERMISSION_GRANTED }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = createFile()

        val uri: Uri = FileProvider.getUriForFile(
            requireContext(),
            "dk.arongk.and1_recipeapp.FileProvider",
            file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun checkPermission(): Boolean {
        return (
                checkSelfPermission(requireContext(), CAMERA)
                        == PackageManager.PERMISSION_GRANTED
                        &&
                        checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(READ_EXTERNAL_STORAGE, CAMERA),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun addIngredient() {
        val element = IngredientListItemCreateModel()
        vm.ingredients.add(element)
        addIngredientListItem(element)
    }

    private fun initializeWidgets(view: View) {
        title = view.findViewById(R.id.title)
        workTime = view.findViewById(R.id.workTime)
        totalTime = view.findViewById(R.id.totalTime)
        servings = view.findViewById(R.id.servings)
        instructions = view.findViewById(R.id.instructions)
        notes = view.findViewById(R.id.notes)
        recipeImage = view.findViewById(R.id.recipeImage)
        recipeImage.visibility = if (vm.imageUri.isBlank()) View.GONE else View.VISIBLE
        createButton = view.findViewById(R.id.createButton)
        addImageButton = view.findViewById(R.id.addImageButton)
        addImageButton.text = if (vm.imageUri.isBlank()) addImageButton.text else getString(R.string.change_image)
        addIngredientButton = view.findViewById(R.id.addIngredientButton)

        createButton.setOnClickListener(this)
        addImageButton.setOnClickListener(this)
        addIngredientButton.setOnClickListener(this)

        ingredientsLinearLayout = view.findViewById(R.id.create_ingredientsLinearLayout)
    }

    private fun initializeIngredientList() {
        vm.ingredients.forEach {
            addIngredientListItem(it)
        }
    }

    private fun addIngredientListItem(it: IngredientListItemCreateModel) {
        val ingredientLL = LinearLayout(requireContext())
        ingredientLL.orientation = LinearLayout.HORIZONTAL
        ingredientLL.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        val quantity = EditText(requireContext())
        quantity.hint = getString(R.string.quantity__short)
        quantity.setText(it.quantity.let { if (it == 0f) "" else it.toString() })
        quantity.inputType = EditorInfo.TYPE_CLASS_NUMBER

        val unit = Spinner(requireContext())
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.units_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            unit.adapter = adapter
            unit.setSelection(adapter.getPosition(it.unit))
        }

        val name = EditText(requireContext())
        name.hint = getString(R.string.ingredient)
        name.setText(it.ingredientName)

        val operation = EditText(requireContext())
        operation.hint = getString(R.string.operation)
        operation.setText(it.operation)

        ingredientLL.addView(quantity)
        ingredientLL.addView(unit)
        ingredientLL.addView(name)
        ingredientLL.addView(operation)

        ingredientValues.add(
            IngredientValues(
                qty = quantity,
                unit = unit,
                name = name,
                operation = operation
            )
        )

        ingredientsLinearLayout.addView(ingredientLL)
    }

    private fun saveToViewModel() {
        vm.title = title.text.toString()
        vm.workTime = workTime.text.toString()
        vm.totalTime = totalTime.text.toString()
        vm.servings = servings.text.toString()
        vm.instructions = instructions.text.toString()
        vm.notes = notes.text.toString()
        vm.imageUri = imageUri

        vm.ingredients.clear()
        ingredientValues.forEach { ingredientValue ->
            vm.ingredients.add(
                IngredientListItemCreateModel().also { model ->
                    model.quantity = ingredientValue.qty.text.toString().toFloatOrNull() ?: 0f
                    model.ingredientName = ingredientValue.name.text.toString()
                    model.unit = ingredientValue.unit.selectedItem.toString()
                    model.operation = ingredientValue.operation.text.toString()
                }
            )
        }
    }

    private fun restoreFromViewModel() {
        title.setText(vm.title)
        workTime.setText(vm.workTime)
        totalTime.setText(vm.totalTime)
        servings.setText(vm.servings)
        instructions.setText(vm.workTime)
        notes.setText(vm.notes)
        recipeImage.setImageURI(Uri.parse(vm.imageUri))
        imageUri = vm.imageUri
    }

    private fun clearViewModel() {
        vm.title = ""
        vm.workTime = ""
        vm.totalTime = ""
        vm.servings = ""
        vm.instructions = ""
        vm.notes = ""
        vm.imageUri = ""
        vm.ingredients.clear()
        ingredientValues.clear()
        initializeIngredientList()
        restoreFromViewModel()
    }

    companion object {
        private const val LOG_TAG = "CREATE_FRAGMENT"
        private const val PERMISSION_REQUEST_CODE = 2
        private const val REQUEST_IMAGE_CAPTURE = 3
    }
}