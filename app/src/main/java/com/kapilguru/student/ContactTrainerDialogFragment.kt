package com.kapilguru.student

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.contact_trainer_dialog.*


class ContactTrainerDialogFragment : DialogFragment() {
    var phoneNumber: String? = null
    var trainerName: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return LayoutInflater.from(context).inflate(R.layout.contact_trainer_dialog, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getIntentData()
        trainer_name.text = trainerName?.toUpperCase()
        contact_trainer_number.text = phoneNumber

        contact_trainer_number.setOnClickListener {
            trainerName?.let {
                dismiss()
                phoneNumber?.let { number->
                    contactPhoneIntent(this.requireContext(),number)
                }

//                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
//                startActivity(intent)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }

    private fun getIntentData() {
        trainerName = arguments?.getString(PARAM_CONTACT_TRAINER_NAME)
        phoneNumber = arguments?.getString(PARAM_CONTACT_TRAINER_NUMBER)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismiss()

    }
}