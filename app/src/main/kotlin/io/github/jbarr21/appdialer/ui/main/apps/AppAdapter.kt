package io.github.jbarr21.appdialer.ui.main.apps

import android.graphics.Color.TRANSPARENT
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.github.jbarr21.appdialer.R
import io.github.jbarr21.appdialer.data.App
import io.github.jbarr21.appdialer.ui.main.dialer.QueryStream
import io.github.jbarr21.appdialer.util.GlideApp
import io.github.jbarr21.appdialer.util.Truss

class AppAdapter(
  callback: DiffUtil.ItemCallback<App>,
  private val queryStream: QueryStream,
  private val clickStream: AppClickStream
) : ListAdapter<App, AppViewHolder>(callback) {

  enum class ImageLoader { PICASSO, GLIDE }
  companion object {
    val imageLoader = ImageLoader.PICASSO
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
    return AppViewHolder(
      LayoutInflater.from(parent.context).inflate(
        R.layout.item_app_grid,
        parent,
        false
      )
    )
  }

  override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
    val app = getItem(position)
    holder.apply {
      title.text = Truss()
        .pushSpan(UnderlineSpan())
        .append(app.label.substring(0, queryStream.currentQuery().size))
        .popSpan()
        .append(app.label.substring(queryStream.currentQuery().size))
        .build()
      itemView.setOnClickListener { clickStream.onClick(app) }
      itemView.setOnLongClickListener { clickStream.onLongClick(app) }

      if (app.iconColor != TRANSPARENT) {
        itemView.setBackgroundColor(app.iconColor)
      }

      when (imageLoader) {
        ImageLoader.PICASSO -> Picasso.get()
          .load(app.uri)
          .placeholder(R.drawable.app_icon_placeholder)
          .into(holder.icon)
        ImageLoader.GLIDE -> GlideApp
          .with(holder.itemView)
          .load(app.uri)
          .placeholder(R.drawable.app_icon_placeholder)
          .into(holder.icon)
      }
    }
  }

  override fun onViewRecycled(holder: AppViewHolder) {
    super.onViewRecycled(holder)
    when (imageLoader) {
      ImageLoader.PICASSO -> Picasso.get().cancelRequest(holder.icon)
      ImageLoader.GLIDE -> GlideApp.with(holder.itemView).clear(holder.icon)
    }
  }
}

class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val icon = itemView.findViewById<ImageView>(R.id.icon)
  val title = itemView.findViewById<TextView>(R.id.text1)
}

class AppDiffCallback : DiffUtil.ItemCallback<App>() {
  override fun areItemsTheSame(oldItem: App, newItem: App) = oldItem.name == newItem.name
  override fun areContentsTheSame(oldItem: App, newItem: App) = false
}