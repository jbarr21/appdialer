package io.github.jbarr21.appdialer.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import io.github.jbarr21.appdialer.app.AppDialerApplication

@GlideModule
class GlideConfig : AppGlideModule() {

  override fun applyOptions(context: Context, builder: GlideBuilder) {
    builder.setDefaultRequestOptions(
      RequestOptions()
        .format(DecodeFormat.PREFER_ARGB_8888))
  }

  override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
    super.registerComponents(context, glide, registry)
    registry.prepend(Uri::class.java, Drawable::class.java, AppDialerApplication.component(context).appIconDecoder())
  }

  override fun isManifestParsingEnabled() = false
}
