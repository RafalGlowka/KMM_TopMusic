package com.glowka.rafal.topmusic.domain.repository

import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.model.Country
import kotlinx.coroutines.flow.StateFlow

interface MusicRepository {

  val country: StateFlow<Country>
  val albums: StateFlow<List<Album>>
  suspend fun initWithLocalStorage(): Result<Boolean>
  suspend fun changeCountryWithLocalStorage(country: Country): Result<Boolean>
  suspend fun reloadFromBackend(): Result<List<Album>>
}
