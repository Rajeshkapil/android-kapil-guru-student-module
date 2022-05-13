package com.kapilguru.student.ui.profileInfo.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.kapilguru.student.R
import com.kapilguru.student.ui.profileInfo.models.CountryResponceItem
import kotlinx.android.synthetic.main.countryname_spinner_item.view.*

class ProfileCustomSpinnerAdapter(
    context: Context,
    countryList: List<CountryResponceItem>,
    isForCountryCode: Boolean
) :
    ArrayAdapter<CountryResponceItem>(context, 0, countryList) {
    var isForCountryCode = isForCountryCode

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val countryItem = getItem(position)
        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.countryname_spinner_item,
            parent,
            false
        )
        if (isForCountryCode) {
            view.text.text = countryItem?.sortName+"(+"+countryItem?.phoneCode+")"
        } else {
            view.text.text = countryItem?.name.toString()
        }
        return view
    }
}