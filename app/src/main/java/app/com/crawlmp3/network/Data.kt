
package app.com.crawlmp3.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data (
		private val items : List<Song>?=null,
		val song : List<Song>?=items
//	val customied : List<String>?=null,
//	val peak_score : Int,
	//val songHis : SongHis
): Parcelable