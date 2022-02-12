package app.com.crawlmp3.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Element (
    val id : String,
    val name: String,
    val artist: String?=null,
    val thumbnail: String
    ) : Parcelable