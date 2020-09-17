package io.github.jbarr21.appdialer.ui.main.dialer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.jbarr21.appdialer.R

class DialerAdapter(
  callback: DiffUtil.ItemCallback<DialerButton>,
  dialerLabels: List<DialerButton>,
  val onLongClick: (DialerButton) -> Unit,
  val onClick: (DialerButton) -> Unit
) : ListAdapter<DialerButton, DialerButtonViewHolder>(callback) {

  private val NUM_ROWS = 3

  init {
    submitList(dialerLabels)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialerButtonViewHolder {
    return DialerButtonViewHolder(
      LayoutInflater.from(parent.context).inflate(
        R.layout.item_dialer,
        parent,
        false
      ).apply {
        minimumHeight = parent.measuredHeight / NUM_ROWS
      }
    )
  }

  override fun onBindViewHolder(holder: DialerButtonViewHolder, position: Int) {
    val button = getItem(position)
    holder.apply {
      label.text = button.label
      itemView.setOnClickListener { onClick(button) }
      itemView.setOnLongClickListener { onLongClick(button).let { true } }
    }
  }
}

class DialerButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val label = itemView.findViewById<TextView>(R.id.label)
}

class DialerButtonDiffCallback : DiffUtil.ItemCallback<DialerButton>() {
  override fun areItemsTheSame(oldItem: DialerButton, newItem: DialerButton) = oldItem.label == newItem.label
  override fun areContentsTheSame(oldItem: DialerButton, newItem: DialerButton) = oldItem == newItem
}