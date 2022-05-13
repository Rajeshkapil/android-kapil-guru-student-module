package com.kapilguru.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.two_dialog_button.view.*

class ChoosePictureDialog(var ChoosePictureDialogInteractor:ChoosePictureDialogInteractor) : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.two_dialog_button, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.IVClose.setOnClickListener {
            dismiss()
        }

        view.TVCamera.setOnClickListener {
            dismiss()
            ChoosePictureDialogInteractor.didClickOnCamera(true)

        }

        view.TVGallery.setOnClickListener {
            dismiss()
            ChoosePictureDialogInteractor.didClickOnCamera(false)
        }
    }

    override fun dismiss() {
        super.dismiss()

    }

}

interface  ChoosePictureDialogInteractor {
        fun didClickOnCamera(isCameraClicked: Boolean)
}