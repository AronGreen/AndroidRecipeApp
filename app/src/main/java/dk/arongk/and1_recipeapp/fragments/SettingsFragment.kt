package dk.arongk.and1_recipeapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import dk.arongk.and1_recipeapp.R

class SettingsFragment : Fragment(), View.OnClickListener {
    private lateinit var signOutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        initializeWidgets(view)

        return view
    }

    override fun onClick(v: View?) {
        Log.d(LOG_TAG, "Button clicked with id: " + (v?.id ?: "null"))
        when (v?.id) {
            R.id.signOutButton -> signOut()
        }
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(requireContext())
            .addOnCompleteListener {
                Toast.makeText(requireContext(), "Signed out", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_settingsFragment_to_mainFragment)
            }
    }

    private fun initializeWidgets(view: View) {
        signOutButton = view.findViewById(R.id.signOutButton)
        signOutButton.setOnClickListener(this)
    }

    companion object {
        private const val LOG_TAG = "SettingsFragment"
    }
}