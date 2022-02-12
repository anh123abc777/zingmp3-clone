package app.com.crawlmp3.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RawSearch (
        val data: List<Albums>?=null
        ) : Parcelable

@Parcelize
data class Albums(
        val album: List<Album>?=null,
        val song: List<Song>?=null,
        val artist: List<Artist>?=null
) : Parcelable