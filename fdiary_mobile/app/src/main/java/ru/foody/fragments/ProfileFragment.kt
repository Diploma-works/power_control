package ru.foody.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import ru.foody.R
import ru.foody.api.DAO
import ru.foody.ui.login.LoginActivity

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.findViewById<Button>(R.id.logoutBtn).setOnClickListener {
            DAO.userRepository.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

            Toast.makeText(
                requireContext(),
                R.string.logoutSuccess,
                Toast.LENGTH_LONG
            ).show()
        }
        return view
    }
}