package jp.shiita.websockettest

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TmpData(val message: String)