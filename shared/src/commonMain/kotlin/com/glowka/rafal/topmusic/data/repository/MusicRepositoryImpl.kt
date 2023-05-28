package com.glowka.rafal.topmusic.data.repository

import com.glowka.rafal.topmusic.data.api.ListApi
import com.glowka.rafal.topmusic.data.api.makeApiCall
import com.glowka.rafal.topmusic.data.database.ConfigKeys
import com.glowka.rafal.topmusic.data.database.MusicDatabase
import com.glowka.rafal.topmusic.data.mapper.AlbumData
import com.glowka.rafal.topmusic.data.mapper.AlbumDsoToAlbumMapper
import com.glowka.rafal.topmusic.data.mapper.AlbumDtoToAlbumMapper
import com.glowka.rafal.topmusic.data.mapper.AlbumToAlbumDsoMapper
import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.model.Country
import com.glowka.rafal.topmusic.domain.repository.MusicRepository
import com.glowka.rafal.topmusic.domain.utils.EMPTY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class MusicRepositoryImpl(
  private val listApi: ListApi,
  val albumDtoToAlbumMapper: AlbumDtoToAlbumMapper,
  val albumDsoToAlbumMapper: AlbumDsoToAlbumMapper,
  val albumToAlbumDsoMapper: AlbumToAlbumDsoMapper,
  private val musicDatabase: MusicDatabase,
) : MusicRepository {

  override val country = MutableStateFlow(Country.UnitedStates)
  override val albums = MutableStateFlow<List<Album>>(emptyList())

  override suspend fun initWithLocalStorage(): Result<Boolean> {
    return Result.success(
      withContext(Dispatchers.IO) {
      val pickedCountry = getCountryFromLocalStorage()
      country.emit(pickedCountry)
      getAlbumsFromLocalStore(pickedCountry)
    }
    )
  }

  override suspend fun changeCountryWithLocalStorage(country: Country): Result<Boolean> {
    return Result.success(
      withContext(Dispatchers.IO) {
        this@MusicRepositoryImpl.country.emit(country)
        getAlbumsFromLocalStore(country)
      }
    )
  }

  override suspend fun reloadFromBackend(): Result<List<Album>> {
    val countryCode = country.value.countryCode
    return makeApiCall {
      val response = listApi.getAlbums(countryCode = countryCode)
      val copyright = response.feed?.copyright ?: String.EMPTY
      response.feed?.results?.mapNotNull { albumDto ->
        val albumData = albumDto?.let { album ->
          AlbumData(album = album, countryCode = countryCode, copyright = copyright)
        }
        albumDtoToAlbumMapper(albumData)
      } ?: emptyList()
    }.onSuccess { newList ->
      albums.emit(newList)
      withContext(Dispatchers.IO) {
        saveAlbumsToLocalStore(newList)
      }
    }
  }

  private suspend fun getCountryFromLocalStorage(): Country {
    val country = musicDatabase.getConfigKey(ConfigKeys.COUNTRY_CODE)
      ?.let { countryCode ->
        Country.values()
          .firstOrNull { country -> country.countryCode == countryCode }
      }
    return country ?: Country.UnitedStates
  }

  private suspend fun getAlbumsFromLocalStore(country: Country): Boolean {
    val list = musicDatabase.getAlbumsByCountryCode(countryCode = country.countryCode)
      .map { albumDso ->
        albumDsoToAlbumMapper(albumDso)
      }
    if (list.isNotEmpty()) {
      albums.emit(list)
    }
    return list.isNotEmpty()
  }

  private suspend fun saveAlbumsToLocalStore(list: List<Album>): Boolean {
    list.firstOrNull()?.run {
      musicDatabase.deleteAlbumsWithCountryCode(countryCode)
    }
    list.map { album ->
      val albumDso = albumToAlbumDsoMapper(album)
      musicDatabase.insertAlbum(albumDso)
    }
    return true
  }
}
