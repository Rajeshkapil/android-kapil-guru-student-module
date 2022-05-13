package com.kapilguru.student.wallet.earningsDetails

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kapilguru.student.*
import com.kapilguru.student.databinding.ActivityAmountViewDetailsBinding
import com.kapilguru.student.network.RetrofitNetwork
import com.kapilguru.student.wallet.model.EarningsRefundList
import kotlinx.android.synthetic.main.activity_amount_view_details.*

class AmountViewDetails : BaseActivity() {

    lateinit var binding: ActivityAmountViewDetailsBinding
    lateinit var viewModel: AmountViewModel
    lateinit var dialog: CustomProgressDialog
    lateinit var amountDetailsRecycler: AmountDetailsRecycler
    private val TAG = "AmountViewDetails"
    var shouldReadAvailable: Boolean = true
    var isFromHistory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_amount_view_details)
        viewModel = ViewModelProvider(
            this, AmountViewModelFactory(
                ApiHelper(RetrofitNetwork.API_KAPIL_TUTOR_SERVICE_SERVICE), application
            )
        ).get(AmountViewModel::class.java)

        setCustomActionBarListener()
        dialog = CustomProgressDialog(this)
        setRecycler()
        readIntent()
        viewModelObserver()
    }

    private fun setRecycler() {
        amountDetailsRecycler = AmountDetailsRecycler()
        recycler.adapter = amountDetailsRecycler
    }

    private fun readIntent() {
        val data: ArrayList<EarningsRefundList>? = intent.getParcelableArrayListExtra<EarningsRefundList>(PARAM_REFUND_LIST)
        if (data != null) {
            amountDetailsRecycler.listOFEarningsDetailsCourse = data
        }
    }


    private fun viewModelObserver() {


    }


    private fun setCustomActionBarListener() {
        setActionbarBackListener(this, binding.actionbar.root, getString(R.string.wallet))
    }


    private fun showErrorDialog(message: String) {

        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                        setCancelable(true)
                    })

                setMessage(message)
            }
            // Create the AlertDialog
            builder.create()
        }
        alertDialog.show()
    }

}