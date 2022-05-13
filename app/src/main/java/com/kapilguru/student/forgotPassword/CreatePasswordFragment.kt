package com.kapilguru.student.forgotPassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kapilguru.student.CustomProgressDialog
import com.kapilguru.student.R
import com.kapilguru.student.databinding.FragmentCreatePasswordBinding
import com.kapilguru.student.forgotPassword.viewModel.ForgotPasswordViewModel
import com.kapilguru.student.network.Status

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CreatePasswordFragment : Fragment() {
    private val TAG = "CreatePasswordFragment"
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding : FragmentCreatePasswordBinding
    val viewModel : ForgotPasswordViewModel by activityViewModels()
    lateinit var dialog : CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        dialog = CustomProgressDialog(requireContext())
        observeViewModelData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_password,container,false)
        binding.viewModel = viewModel
        setClickListeners()
        return binding.root
    }

    private fun observeViewModelData(){
        viewModel.changePasswordResMutLiveData.observe(this, Observer {
            when(it.status){
                Status.LOADING -> {
                    dialog.showLoadingDialog()
                }

                Status.SUCCESS -> {
                    dialog.dismissLoadingDialog()
                    Toast.makeText(requireContext(),"Changed Password Successfully",Toast.LENGTH_LONG).show()
                    activity?.finish()
                }

                Status.ERROR -> {
                    dialog.dismissLoadingDialog()
                }
            }
        })
    }

    private fun setClickListeners(){
        binding.btnConfirmPassword.setOnClickListener {
            viewModel.changePassword()
        }
        binding.btnCancel.setOnClickListener {
            binding.etPassword.setText("")
            binding.etConformPassword.setText("")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreatePasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        @JvmStatic
        fun noInstance() = CreatePasswordFragment()
    }
}