package com.glowka.rafal.topmusic.data.dso

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ConfigKeyDso : RealmObject {
    @PrimaryKey
    var key: String = ""
    var value: String = ""
}