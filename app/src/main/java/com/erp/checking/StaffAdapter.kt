package com.erp.checking

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.erp.checking.network.response.SearchStaffResponse

internal class StaffAdapter(context: Context) :
    ArrayAdapter<SearchStaffResponse.Staff>(context, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        var row = convertView
        if (row == null) {
            row = (context as Activity).layoutInflater.inflate(R.layout.item_staff, parent, false)
            holder = ViewHolder()
            holder.mCountry = row!!.findViewById<View>(R.id.spinnerTextCab) as TextView
            row.tag = holder
        } else {
            holder = row.tag as ViewHolder
        }
        val model = getItem(position)
        holder.mCountry!!.text = model.name
        return row
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        var row = convertView
        if (row == null) {
            row = (context as Activity).layoutInflater.inflate(R.layout.item_staff, parent, false)
            holder = ViewHolder()
            holder.mCountry = row!!.findViewById<View>(R.id.spinnerTextCab) as TextView
            row.tag = holder
        } else {
            holder = row.tag as ViewHolder
        }
        val model = getItem(position)
        holder.mCountry!!.text = model.name
        return row
    }

    internal inner class ViewHolder {
        var mCountry: TextView? = null
    }
}
