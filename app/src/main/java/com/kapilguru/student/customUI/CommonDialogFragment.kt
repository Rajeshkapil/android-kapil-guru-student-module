package com.kapilguru.student.customUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kapilguru.student.databinding.FragmentComplaintRefundBinding


class CommonDialogFragment() : DialogFragment() {
    lateinit var listener: CommonDialogListener
    lateinit var binding: FragmentComplaintRefundBinding

    var title: String?=null
    var hintText: String?=null
    var isSubTitle: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString("Title")
            hintText = it.getString("hintText")
            isSubTitle = it.getBoolean("isSubTitleRequired")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComplaintRefundBinding.inflate(inflater,container,false)
        dialog?.setCanceledOnTouchOutside(false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindDialogData()
        setOnClickListener()
    }

    private fun bindDialogData() {
        binding.dialogueTitle.text = title
        binding.textInputLayout.hint = hintText
        binding.refundNote.visibility = if (isSubTitle) View.VISIBLE else View.GONE
    }

    fun setDialogueListener(listener: CommonDialogListener) {
         this.listener = listener
     }

    private fun setOnClickListener() {
        binding.iconClose.setOnClickListener { dialog?.cancel() }

        binding.button.setOnClickListener {
            listener.onReasonSubmitted(binding.etReason.text.toString().trim())
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(title: String,hintText:String,isSubTitleRequired: Boolean) =
            CommonDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("Title",title)
                    putString("hintText",hintText)
                    putBoolean("isSubTitleRequired",isSubTitleRequired)
                }
        }
    }

    fun clearText(){
        if (this::binding.isInitialized) binding.etReason.text?.clear()
    }

    fun shouldShowLoading(loading: Boolean){
        if (this::binding.isInitialized) binding.button.shouldShowLoading(loading)
    }

    interface CommonDialogListener{
        fun onReasonSubmitted(reason: String)
    }
}