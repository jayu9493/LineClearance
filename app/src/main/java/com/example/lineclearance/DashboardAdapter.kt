package com.example.lineclearance

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class DashboardAdapter : ListAdapter<LineClearancePermit, DashboardAdapter.PermitViewHolder>(PermitDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermitViewHolder {
        return PermitViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PermitViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class PermitViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val feederName: TextView = itemView.findViewById(R.id.feeder_name)
        private val substationName: TextView = itemView.findViewById(R.id.substation_name)
        private val statusText: TextView = itemView.findViewById(R.id.status_text)
        private val lcNumberText: TextView = itemView.findViewById(R.id.lc_number_text)
        // private val statusIcon: ImageView = itemView.findViewById(R.id.status_icon) // Commented out to prevent build error

        fun bind(item: LineClearancePermit) {
            feederName.text = item.feederName
            substationName.text = item.substationName
            statusText.text = item.status
            lcNumberText.text = "LC #" + (item.lcNumber ?: "N/A")

            when (item.status) {
                "Live" -> {
                    statusText.setTextColor(Color.GREEN)
                    // TODO: Set statusIcon to a 'live' drawable
                }
                "Requested" -> {
                    statusText.setTextColor(Color.BLUE)
                    // TODO: Set statusIcon to a 'requested' drawable
                }
                else -> {
                    statusText.setTextColor(Color.GRAY)
                    // TODO: Set statusIcon to a default drawable
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): PermitViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.permit_list_item, parent, false)
                return PermitViewHolder(view)
            }
        }
    }
}

class PermitDiffCallback : DiffUtil.ItemCallback<LineClearancePermit>() {
    override fun areItemsTheSame(oldItem: LineClearancePermit, newItem: LineClearancePermit): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LineClearancePermit, newItem: LineClearancePermit): Boolean {
        return oldItem == newItem
    }
}