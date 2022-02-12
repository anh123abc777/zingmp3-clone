package app.com.crawlmp3.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artist(
    val name: String?="Unknown",
    @Json(name="thumb") val thumbnail: String?="",
    val id : String?=""
    ) : Parcelable