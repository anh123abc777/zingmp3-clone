/* 
Copyright (c) 2021 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */

package app.com.crawlmp3.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Song (

	val id : String?="",
	val name : String?="",
	val title : String?="",
	val code : String?="",
	val content_owner : Int?=0,
	//val isoffical : Boolean,
	//val isWorldWide : Boolean,
//	val playlist_id : String?=null,
	//val artists : List<Artists>,
	val artists_names : String?="",
	val performer : String?="",
	val type : String?="",
	val link : String?="",
	val lyric : String?="",
	val thumb : String?="",
	val thumbnail : String?=thumb,
	val duration : Int?=0,
	val total : Int?=0,
	val rank_num : String?="",
	val rank_status : String?="",
	//val artist : Artist?=null,
	val position : Int?=0,
	//val order : Int,
	//val album : Album?=null
) : Parcelable