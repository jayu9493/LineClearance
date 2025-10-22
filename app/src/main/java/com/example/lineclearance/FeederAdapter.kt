package com.example.lineclearance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FeederAdapter(
    private val feeders: List<Feeder>,
    private val onItemClick: (Feeder) -> Unit
) :
    RecyclerView.Adapter<FeederAdapter.FeederViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeederViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return FeederViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeederViewHolder, position: Int) {
        val feeder = feeders[position]
        holder.bind(feeder)
        holder.itemView.setOnClickListener {
            onItemClick(feeder)
        }
    }

    override fun getItemCount(): Int = feeders.size

    class FeederViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(feeder: Feeder) {
            nameTextView.text = feeder.name
        }
    }
}