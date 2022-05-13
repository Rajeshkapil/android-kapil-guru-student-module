package com.kapilguru.student.searchCourse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.kapilguru.student.R
import com.kapilguru.student.searchCourse.model.AutoSearchModelApi


class CustomArrayAdapter(
    var mContext: Context,
    var customArraySearchItem: Int,
    var autoSearchList: ArrayList<AutoSearchModelApi>
) : ArrayAdapter<AutoSearchModelApi>(
    mContext, customArraySearchItem, autoSearchList
) {
    var suggestions: ArrayList<AutoSearchModelApi> = arrayListOf()


    override fun getCount(): Int {
        return autoSearchList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        if (convertView == null) {
            val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.custom_array_search_item, parent, false)
        }
        val autoSearchModelApi: AutoSearchModelApi = autoSearchList[position]
        val courseTitile = view?.findViewById(R.id.courseTitle) as TextView
        courseTitile.text = autoSearchModelApi.courseTitle

        return view
    }


    override fun getFilter(): Filter {
        return myFilter
    }


    val myFilter = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return if (constraint != null) {
                for (autoSearchModelApi in autoSearchList) {
                    if (autoSearchModelApi.courseTitle.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(autoSearchModelApi)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                filterResults
            } else {
                FilterResults()
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            results?.values?.let {it->
                val filterList: ArrayList<AutoSearchModelApi> = it as ArrayList<AutoSearchModelApi>
                if (results?.count > 0) {
                    clear()
                    filterList?.let {
                        for (autoSearchModelApi in it) {
                            add(autoSearchModelApi)
                            notifyDataSetChanged()
                        }
                    }
                }
            }

        }

        override fun convertResultToString(resultValue: Any): CharSequence {
            return (resultValue as AutoSearchModelApi).courseTitle
        }

    }

}