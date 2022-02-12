package app.com.crawlmp3

import android.net.Uri
import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import app.com.crawlmp3.adapter.ArtistAdapter
import app.com.crawlmp3.weeklyrankings.ListSongAdapter
import app.com.crawlmp3.network.Song
import app.com.crawlmp3.home.HomeAdapter
import app.com.crawlmp3.network.Album
import app.com.crawlmp3.network.RawSearch
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import timber.log.Timber

@BindingAdapter(value=["listData","raw"],requireAll = false)
fun bindRecyclerView(
    recyclerView: RecyclerView,
    data : List<Song>?=null,
    raw: RawSearch?=null){
    val adapter = recyclerView.adapter as ListSongAdapter
    if(data!=null) {
        adapter.submitList(data)
    } else{
        raw?.data?.forEach {
            if (it.song!=null) {
                adapter.submitList(it.song)
            }
        }
    }
}

@BindingAdapter("imageUrl")
fun bindImage(imgView : ImageView, imgUrl : String?){
    imgUrl?.let {
        val imgUri : Uri = if (imgUrl.startsWith("cover") ||
            imgUrl.startsWith("avatars")){
            val newImgUrl = "https://photo-resize-zmp3.zadn.vn/w320_r1x1_jpeg/$imgUrl"
            newImgUrl.toUri().buildUpon().build()
        } else{
            val newImgUrl=imgUrl.replace("w94_r1x1_jpeg","w320_r1x1_jpeg",false)
            Timber.i(newImgUrl)
            newImgUrl.toUri().buildUpon().scheme("https").build()
        }
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}

@BindingAdapter("formatTime")
fun bindTime(textView: TextView,timeInt : Int?){
    timeInt?.let {
        textView.text = DateUtils.formatElapsedTime(timeInt.toLong())
    }
}

@BindingAdapter(value = ["albumGroup","raw"],requireAll = false)
fun bindAlbumGroup(recyclerView: RecyclerView,
                   data: List<Album>?=null,
                    raw: RawSearch?=null){

    val adapter = recyclerView.adapter as HomeAdapter
    if (data!=null) {
        adapter.submitList(data)
    }else{
        raw?.data?.forEach {
            if (it.album!=null) {
                adapter.submitList(it.album)
            }
        }
    }
}

@BindingAdapter("setVisibility")
fun isRank(textView: TextView,position : Int){
    if(position==0){
        textView.visibility = View.INVISIBLE
    }
    else{
        textView.visibility = View.VISIBLE
    }
}

@BindingAdapter("artistGroup")
fun bindArtistGroup(recyclerView: RecyclerView,
                   raw: RawSearch?=null){

    val adapter = recyclerView.adapter as ArtistAdapter
    raw?.data?.forEach {
        if (it.artist!=null) {
            adapter.submitList(it.artist)
        }
    }
}

@BindingAdapter("visibilityLabel")
fun visibilityLabel(textView: TextView,recyclerView: RecyclerView){
    if(recyclerView.visibility == View.VISIBLE){
        textView.visibility = View.VISIBLE
    } else{
        textView.visibility = View.INVISIBLE
    }
}

@BindingAdapter("setVisibilityLayout")
fun setVisibilityLayout(layout: RelativeLayout, song: Song?){
    layout.visibility = if(song != null) View.VISIBLE else View.GONE
}

