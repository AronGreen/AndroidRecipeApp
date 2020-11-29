package dk.arongk.and1_recipeapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dk.arongk.and1_recipeapp.R

// Authentication code adapted from: https://github.com/firebase/snippets-android/blob/686d8e61edab387ae35c3b6cb2d666b936d54f79/auth/app/src/main/java/com/google/firebase/quickstart/auth/kotlin/FirebaseUIActivity.kt#L21-L35
// following: https://firebase.google.com/docs/auth/android/firebaseui?authuser=0
private const val LOG_TAG = "MainFragment"

class MainFragment : Fragment(), View.OnClickListener {

    private lateinit var signInButton :Button
    private var bottomNav : BottomNavigationView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        // If user is already authenticated, redirect
        if (FirebaseAuth.getInstance().currentUser != null)
            authenticationSuccess()
        else
            initializeWidgets(view)

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                val username = user?.displayName ?: "Anonymous"
                Toast.makeText(requireContext(), "Welcome $username", Toast.LENGTH_LONG).show()
                authenticationSuccess()
            } else {
                if (response == null)
                    Toast.makeText(requireContext(), "Sign in cancelled", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(requireContext(), response.error?.localizedMessage ?: "No error message found", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClick(v: View?) {
        Log.d(LOG_TAG, "Button clicked with id: " + v?.id)
        when (v?.id) {
            R.id.signInButton -> createSignInIntent()
        }
    }

    private fun authenticationSuccess(){
        bottomNav?.visibility = View.VISIBLE
        findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
    }

    private fun createSignInIntent() {
//        Toast.makeText(requireContext(), "Signing in...", Toast.LENGTH_LONG).show()
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    private fun initializeWidgets(view: View) { //TODO: verify that 'vm' parameter is unnecessary here and remove accordingly
        signInButton = view.findViewById(R.id.signInButton)
        signInButton.setOnClickListener(this)

        // Remove navigation for this fragment, is reinstated on authentication success
        bottomNav = activity?.findViewById(R.id.bottom_navigation)
        bottomNav?.visibility = View.INVISIBLE
    }

    companion object {
        private const val RC_SIGN_IN = 99
    }
}