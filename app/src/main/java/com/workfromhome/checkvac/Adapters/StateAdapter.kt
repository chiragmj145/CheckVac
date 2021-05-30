package com.workfromhome.checkvac.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.workfromhome.checkvac.Models.StatesModel
import com.workfromhome.checkvac.R

class StateAdapter(private val stateList: List<StatesModel>) :
    RecyclerView.Adapter<StateAdapter.StateViewHolder>() {

    class StateViewHolder(itemView: View) : ViewHolder(itemView) {
        val stateName = itemView.findViewById<TextView>(R.id.stateNameTextView)
        val cases = itemView.findViewById<TextView>(R.id.stateCasesTextView)
        val recovered = itemView.findViewById<TextView>(R.id.stateRecordsTextView)
        val deaths = itemView.findViewById<TextView>(R.id.stateDeathsTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.state_rv_item, parent, false)
        return StateViewHolder(v)
    }

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) {
        val stateData = stateList[position]
        holder.cases.text = stateData.cases.toString()
        holder.deaths.text = stateData.Deaths.toString()
        holder.stateName.text = stateData.state
        holder.recovered.text = stateData.recovered.toString()
    }

    override fun getItemCount(): Int {
        return stateList.size
    }
}
