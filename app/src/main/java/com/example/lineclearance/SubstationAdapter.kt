package com.example.lineclearance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SubstationAdapter(
    private val substations: List<Substation>,
    private val onItemClick: (Substation) -> Unit
) :
    RecyclerView.Adapter<SubstationAdapter.SubstationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubstationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return SubstationViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubstationViewHolder, position: Int) {
        val substation = substations[position]
        holder.bind(substation)
        holder.itemView.setOnClickListener {
            onItemClick(substation)
        }
    }

    override fun getItemCount(): Int = substations.size

    class SubstationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(substation: Substation) {
            nameTextView.text = substation.name
        }
    }
}