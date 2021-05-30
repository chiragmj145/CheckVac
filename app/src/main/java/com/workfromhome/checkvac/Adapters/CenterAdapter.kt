package com.workfromhome.checkvac.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.workfromhome.checkvac.Models.CenterModel
import com.workfromhome.checkvac.R
import kotlinx.android.synthetic.main.vaccine_list.view.*

class CenterAdapter(private val centerList: List<CenterModel>) :
    RecyclerView.Adapter<CenterAdapter.CenterViewHolder>() {
    class CenterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val centerName = itemView.centerNameTextView
        val centerLocation = itemView.centerLocationTextView
        val centerTimings = itemView.centerTimingTextView
        val vaccineName = itemView.vaccineNameTextView
        val vaccineFees = itemView.feesTextView
        val ageLimit = itemView.ageLimitTextView
        val availability = itemView.availabilityTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.vaccine_list, parent, false)
        return CenterViewHolder(v)
    }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        val center = centerList[position]
        holder.centerName.text = center.centerName
        holder.centerLocation.text = center.centerAddress
        holder.centerTimings.text =
            ("From : " + center.centerFromTime + " To :" + center.centerToTime)
        holder.vaccineName.text = center.vaccineName
        holder.vaccineFees.text = center.feeType
        holder.ageLimit.text = ("Age Limit :" + center.ageLimit.toString())
        holder.availability.text = ("Availability :" + center.availableCapacity)
    }

    override fun getItemCount(): Int {
        return centerList.size
    }
}
