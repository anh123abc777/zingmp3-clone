package app.com.crawlmp3.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupPlaylist(
    val title: String,
    var playlists: List<Album>
) : Parcelable
