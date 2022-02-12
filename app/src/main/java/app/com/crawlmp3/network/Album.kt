

package app.com.crawlmp3.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Album (
	val thumb : String,
	val artist: String ? = "UnKnown artist",
	val name : String ,
	val id : String
) : Parcelable