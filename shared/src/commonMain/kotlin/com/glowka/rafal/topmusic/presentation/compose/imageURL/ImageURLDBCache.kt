package com.glowka.rafal.topmusic.presentation.compose.imageURL

import androidx.compose.ui.graphics.ImageBitmap
import com.glowka.rafal.topmusic.presentation.compose.createImageBitmap
import io.realm.kotlin.Realm
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration

interface ImageURLDBCache {
  suspend fun getImage(url: String): ImageBitmap?
  suspend fun setImage(url: String, imageData: ByteArray)
}

class ImageURLDBCacheImpl(
  realm: Realm,
  dataValidDuration: Duration
) : ImageURLDBCache {

  private val coroutineScope = CoroutineScope(Dispatchers.IO)
  private val database = ImageDatabaseImpl(realm)

  init {
    coroutineScope.launch {
      val date = now().minus(dataValidDuration)
      database.deleteOlderThan(RealmInstant.from(date.epochSeconds, date.nanosecondsOfSecond))
    }
  }

  override suspend fun getImage(url: String): ImageBitmap? {
    return database.getImage(url)?.let { image ->
      image.picture?.let { pictureData ->
        coroutineScope.launch {
          setImage(url, pictureData)
        }
//        Logger.d("getDBImage: ${pictureData.size}")
        createImageBitmap(pictureData)
      }
    }
  }

  override suspend fun setImage(url: String, imageData: ByteArray) {
    val image = ImageDso().apply {
      this.url = url
      this.picture = imageData
      this.accessDate = RealmInstant.now()
    }
    database.insertImage(image)
  }
}