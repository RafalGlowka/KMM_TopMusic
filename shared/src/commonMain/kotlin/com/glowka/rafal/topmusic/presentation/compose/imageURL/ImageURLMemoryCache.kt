package com.glowka.rafal.topmusic.presentation.compose.imageURL

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

interface ImageURLMemoryCache {
  suspend fun getImage(url: String): ImageBitmap?
  suspend fun setImage(url: String, image: ImageBitmap)
}

data class ImageCacheItem(
  val url: String,
  val image: ImageBitmap,
  var accessDate: Instant,
)

class ImageURLMemoryCacheImpl(val maxSize: Int) : ImageURLMemoryCache {

  private val mutex = Mutex()
  private val cache = mutableListOf<ImageCacheItem>()
  override suspend fun getImage(url: String): ImageBitmap? {
    mutex.withLock {
      val itemIndex = cache.indexOfFirst { item -> item.url == url }
      if (itemIndex == -1) return null
      val item = cache[itemIndex]
      item.accessDate = Clock.System.now()
      return item.image
    }
  }

  override suspend fun setImage(url: String, image: ImageBitmap) {
    mutex.withLock {
      val item = ImageCacheItem(url, image, Clock.System.now())
      val itemIndex = cache.indexOfFirst { existingItem -> existingItem.url == url }
      if (itemIndex > -1) {
        cache[itemIndex] = item
      } else {
        if (cache.size >= maxSize) reduceCache()
        cache.add(0, item)
      }
    }
  }

  private fun reduceCache() {
    cache.sortByDescending { item -> item.accessDate.epochSeconds }
    cache.removeLast()
  }
}