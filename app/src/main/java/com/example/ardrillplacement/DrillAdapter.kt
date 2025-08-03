package com.example.ardrillplacement

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

data class Drill(
    val name: String,
    val description: String,
    val imageResId: Int,
    val modelPath: String
)

class DrillAdapter(
    private val drills: List<Drill>,
    private val onItemClicked: (Drill) -> Unit
) : RecyclerView.Adapter<DrillAdapter.DrillViewHolder>() {

    private var selectedPosition = 0

    inner class DrillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val drillCard: MaterialCardView = itemView.findViewById(R.id.drill_card)
        val drillImage: ImageView = itemView.findViewById(R.id.drill_image)
        val drillName: TextView = itemView.findViewById(R.id.drill_name)
        val drillDescription: TextView = itemView.findViewById(R.id.drill_description)

        fun bind(drill: Drill, isSelected: Boolean) {
            drillName.text = drill.name
            drillDescription.text = drill.description
            drillImage.setImageResource(drill.imageResId)

            if (isSelected) {
                drillCard.strokeWidth = 6
                drillCard.setCardBackgroundColor(Color.parseColor("#E0E8F0"))
            } else {
                drillCard.strokeWidth = 2
                drillCard.setCardBackgroundColor(Color.WHITE)
            }

            itemView.setOnClickListener {
                onItemClicked(drill)
                if (selectedPosition != adapterPosition) {
                    val previousSelectedPosition = selectedPosition
                    selectedPosition = adapterPosition
                    notifyItemChanged(previousSelectedPosition)
                    notifyItemChanged(selectedPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrillViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drill, parent, false)
        return DrillViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrillViewHolder, position: Int) {
        holder.bind(drills[position], position == selectedPosition)
    }

    override fun getItemCount() = drills.size

    fun getSelectedDrill(): Drill {
        return drills[selectedPosition]
    }
}
