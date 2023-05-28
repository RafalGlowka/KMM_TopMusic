package com.glowka.rafal.topmusic.presentation.compose.imageURL

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import co.touchlab.kermit.Logger
import com.glowka.rafal.topmusic.data.api.makeApiCall
import com.glowka.rafal.topmusic.di.injectOrNull
import com.glowka.rafal.topmusic.presentation.compose.createImageBitmap
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.utils.io.core.use
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private sealed interface ImageState {
  object Loading : ImageState
  data class Loaded(val image: ImageBitmap) : ImageState
  object Failure : ImageState
}

@Suppress("CyclomaticComplexMethod")
@Composable
internal fun ImageURL(
  url: String,
  contentDescription: String,
  modifier: Modifier = Modifier,
  loading: @Composable () -> Unit = { Spacer(modifier) },
  failure: @Composable () -> Unit = { Spacer(modifier) },
  alignment: Alignment = Alignment.Center,
  contentScale: ContentScale = ContentScale.Fit,
  useMemoryCache: Boolean = false,
  useDBCache: Boolean = false,
) {
  val log = Logger.withTag("imageURL")
  var state by remember { mutableStateOf<ImageState>(ImageState.Loading) }
  LaunchedEffect(url) {
    withContext(Dispatchers.IO) {
      val memoryCache: ImageURLMemoryCache? by injectOrNull()
      val dbCache: ImageURLDBCache? by injectOrNull()

      var image: ImageBitmap? = null
      if (useMemoryCache) {
        memoryCache?.getImage(url = url)?.let { memoryImage ->
          log.d("Image from Memory: $url ${memoryImage.width}x${memoryImage.height}")
          image = memoryImage
        }
      }
      if (image == null && useDBCache) {
        dbCache?.getImage(url = url)?.let { dbImage ->
          log.d("Image from DB: $url ${dbImage.width}x${dbImage.height}")
          image = dbImage
          if (useMemoryCache) {
            memoryCache?.setImage(url, dbImage)
          }
        }
      }
      image?.let { loadedImage ->
        state = ImageState.Loaded(image = loadedImage)
      } ?: run {
        makeApiCall { HttpClient().use { client -> client.get(url).body<ByteArray>() } }
          .onSuccess { imageData ->
            image = createImageBitmap(imageData)
            state = image?.let { imageBitmap ->
              log.d("Image from URL: $url")
              launch {
                withContext(Dispatchers.IO) {
                  if (useMemoryCache) {
                    memoryCache?.setImage(url, imageBitmap)
                  }
                  if (useDBCache) {
                    dbCache?.setImage(url, imageData)
                  }
                }
              }
              ImageState.Loaded(image = imageBitmap)
            } ?: ImageState.Failure
          }
          .onFailure { error ->
            Logger.e("imageURL $url", error)
            state = ImageState.Failure
          }
      }
    }
  }
  when (val currentState = state) {
    ImageState.Loading -> loading()
    ImageState.Failure -> failure()
    is ImageState.Loaded -> {
      Image(
        modifier = modifier,
        bitmap = currentState.image,
        contentDescription = contentDescription,
        alignment = alignment,
        contentScale = contentScale,
      )
    }
  }
}
