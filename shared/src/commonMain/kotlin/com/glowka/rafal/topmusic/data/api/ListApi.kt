package com.glowka.rafal.topmusic.data.api

import com.glowka.rafal.topmusic.data.dto.ListResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// https://rss.applemarketingtools.com/api/v2/us/music/most-played/10/albums.json

interface ListApi {
  suspend fun getAlbums(countryCode: String): ListResponseDto
}

class ListApiImpl : ListApi {

  private val baseUrl = "https://rss.applemarketingtools.com"

  private val httpClient = HttpClient {
    install(ContentNegotiation) {
      json(
        Json {
          ignoreUnknownKeys = true
          useAlternativeNames = false
        }
      )
    }
  }

  override suspend fun getAlbums(countryCode: String): ListResponseDto {
    return httpClient.get(
      urlString = "$baseUrl/api/v2/$countryCode/music/most-played/100/albums.json"
    ).body()
  }
}
