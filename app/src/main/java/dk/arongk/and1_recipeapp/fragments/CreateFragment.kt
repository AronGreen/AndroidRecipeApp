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
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.getExternalFilesDirs
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import dk.arongk.and1_recipeapp.R
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
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (allPermissionsGranted(grantResults) ) {
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
            when(requestCode){
                REQUEST_IMAGE_CAPTURE -> {
                    //            val auxFile = File(currentPhotoPath)
                    val bitmap: Bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                    recipeImage.setImageBitmap(bitmap)
                    vm.imageUri = currentPhotoPath
                }
                // TODO: pick from gallery
            }

        // TODO: handle reject

    }

    private fun createRecipe() {
        Log.i(LOG_TAG, "create recipe: " + title.text.toString())
        saveToViewModel()
        vm.insert(
            vm.title,
            vm.workTime.toIntOrNull() ?: 0,
            vm.totalTime.toIntOrNull() ?: 0,
            vm.servings.toIntOrNull() ?: 0,
            vm.description,
            vm.instructions,
            vm.notes,
            vm.imageUri
        )
        clearViewModel()
    }

    private fun initTakePicture() =
        if (checkPermission()) takePicture() else requestPermission()

    private fun allPermissionsGranted(grantResults: IntArray): Boolean {
        return grantResults.isNotEmpty() && grantResults.all { x -> x ==  PackageManager.PERMISSION_GRANTED}
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
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

//    private fun pickImageFromGallery() {
//        //Intent to pick image
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent, IMAGE_PICK_ACTIVITY_REQUEST_CODE)
//    }

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

    private fun saveToViewModel() {
        vm.title = title.text.toString()
        vm.workTime = workTime.text.toString()
        vm.totalTime = totalTime.text.toString()
        vm.servings = servings.text.toString()
        vm.description = description.text.toString()
        vm.instructions = instructions.text.toString()
        vm.notes = notes.text.toString()
        vm.imageUri = imageUri
    }

    private fun restoreFromViewModel() {
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

    private fun clearViewModel() {
        vm.title = ""
        vm.workTime = ""
        vm.totalTime = ""
        vm.servings = ""
        vm.description = ""
        vm.instructions = ""
        vm.notes = ""
        vm.imageUri = ""
        restoreFromViewModel()
    }

    companion object {
        private const val LOG_TAG = "CREATE_FRAGMENT"
        private const val IMAGE_PICK_ACTIVITY_REQUEST_CODE = 1
        private const val PERMISSION_REQUEST_CODE = 2
        private const val REQUEST_IMAGE_CAPTURE = 3
    }
}