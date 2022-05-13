package com.kapilguru.student

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kapilguru.student.login.LoginActivity
import com.kapilguru.student.preferences.StorePreferences
import kotlinx.android.synthetic.main.layout_progress_bar.*
import kotlinx.android.synthetic.main.layout_progress_bar.progressBar_text
import kotlinx.android.synthetic.main.session_logout.*

class SessionLogOutDialogFragment: DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.session_logout, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ok_txt.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        StorePreferences(this.requireActivity()).clearStorePreferences()
        startActivity(Intent(this.requireActivity(),LoginActivity::class.java))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismiss()
    }

}