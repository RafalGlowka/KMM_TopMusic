package com.glowka.rafal.topmusic.domain.model

import com.glowka.rafal.topmusic.MainRes

enum class Country(val countryCode: String) {
  Angola("ao"),
  UnitedStates("us"),
  Poland("pl"),
  UnitedKingdom("gb"),
  France("fr"),
  Germany("de")
}

val Country.countryName: String
  get() {
    return when (this) {
      Country.Angola -> MainRes.string.country_ao
      Country.UnitedStates -> MainRes.string.country_us
      Country.Poland -> MainRes.string.country_pl
      Country.UnitedKingdom -> MainRes.string.country_gb
      Country.France -> MainRes.string.country_fr
      Country.Germany -> MainRes.string.country_de
    }
  }