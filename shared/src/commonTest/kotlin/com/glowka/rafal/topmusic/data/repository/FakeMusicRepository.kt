package com.glowka.rafal.topmusic.data.repository

import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.model.Country
import com.glowka.rafal.topmusic.domain.repository.MusicRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

class FakeMusicRepository : MusicRepository {

  private val _initializing = MutableSharedFlow<Unit>()
  val initializing: SharedFlow<Unit> = _initializing

  private val _reloading = MutableSharedFlow<Unit>()
  val reloading: SharedFlow<Unit> = _reloading

  private var initResponse: Result<Boolean> =
    Result.failure(IllegalStateException("Missing response"))
  private var initDelayMs = 0L
  private var changeCountryResponse: Result<Boolean> =
    Result.failure(IllegalStateException("Missing response"))
  private var changeCountryDelayMs = 0L
  private var reloadResponse: Result<List<Album>> =
    Result.failure(IllegalStateException("Missing response"))
  private var reloadDelayMs = 0L


  override val country = MutableStateFlow(Country.UnitedStates)
  override val albums = MutableStateFlow<List<Album>>(emptyList())

  override suspend fun initWithLocalStorage(): Result<Boolean> {
    _initializing.emit(Unit)
    delay(initDelayMs)
    return initResponse
  }

  override suspend fun changeCountryWithLocalStorage(country: Country): Result<Boolean> {
    delay(changeCountryDelayMs)
    this@FakeMusicRepository.country.emit(country)
    return changeCountryResponse
  }

  override suspend fun reloadFromBackend(): Result<List<Album>> {
    _reloading.emit(Unit)
    delay(reloadDelayMs)
    return reloadResponse.onSuccess { list ->
      albums.emit(list)
    }
  }

  fun setInitResponse(
    initResponse: Result<Boolean>,
    delayMs: Long = 0L,
  ) {
    this.initResponse = initResponse
    this.initDelayMs = delayMs
  }

  fun setReloadResponse(
    reloadResponse: Result<List<Album>>,
    delayMs: Long = 0L,
  ) {
    this.reloadResponse = reloadResponse
    this.reloadDelayMs = delayMs
  }

  fun setChangeCountryResponse(
    changeCountryResponse: Result<Boolean>,
    delayMs: Long = 0L,
  ) {
    this.changeCountryResponse = changeCountryResponse
    this.changeCountryDelayMs = delayMs
  }
}