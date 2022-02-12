
package app.com.crawlmp3.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Raw (
	val data : Data,
	val err : Int,
	val msg : String,
	val timestamp : Long
): Parcelable