package app.com.crawlmp3.playlist

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.com.crawlmp3.function.Crawling
import app.com.crawlmp3.function.DataCrawler
import app.com.crawlmp3.network.Song


class PlaylistViewModel(
                         idAlbum: String?=null) : ViewModel() {


    private var _songs = MutableLiveData<List<Song>>()
    val songs : LiveData<List<Song>>
        get() = _songs

    private var _navigateToSelectedSong =  MutableLiveData<Song>()
    val navigateToSelectedSong : LiveData<Song>
        get() = _navigateToSelectedSong

    fun onSongClick(song : Song){
        _navigateToSelectedSong.value = song
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun onPlayViewNavigated(){
        _navigateToSelectedSong.value = null
    }

//    private var sourceHtmlAlbum = String()

    private val dataCrawler = DataCrawler(idAlbum!!,Crawling.Album)

    init {
//        createEndWithHTMLForIdAlbum()
//        getTracks()
        _songs.value = dataCrawler.getTracks()
    }

//    private fun createEndWithHTMLForIdAlbum(){
//        idAlbum += ".html"
//    }

//    private fun getTracks(){
//        if(idAlbum!="") {
//            getSourceHtmlAlbum()
//            getKeyAlbum()
//            getJSONTracks()
//        }
//    }
//
//    private fun getSourceHtmlAlbum() =runBlocking {
//        try {
//            val getSourceHtmlAlbumDeferred = Mp3ApiViewSource.retrofitServiceViewSource.getKeyAsync(
//                idAlbum!!
//            )
//            Timber.i("getDeferred completed")
//            sourceHtmlAlbum = getSourceHtmlAlbumDeferred.await()
//            Timber.i("getSourceString completed")
//        }
//        catch (e : Exception){
//            Timber.i("error Html ${e.message}")
//        }
//    }
//
//    private fun getKeyAlbum(){
//        try {
//            sourceHtmlAlbum = sourceHtmlAlbum.substringAfter("data-xml=\"")
//            sourceHtmlAlbum = sourceHtmlAlbum.substringBefore("\"")
//            sourceHtmlAlbum = sourceHtmlAlbum.substringAfter("key=")
//            Timber.i(sourceHtmlAlbum)
//        }
//        catch (e : Exception){
//            Timber.i(e.message)
//        }
//    }
//
//    private fun getJSONTracks() = runBlocking {
//        try {
//            val getTracksDeferred = Mp3Api.retrofitService.getPropertiesAsync(
//                    "media","get-source","album",sourceHtmlAlbum)
//            _songs.value = getTracksDeferred.await().data.song!!
//        }
//        catch (e : Exception){
//            Timber.i("error getJSONTracks ${e.message}")
//        }
//    }

//    fun setData(songGroup : List<Song>){
//        _songs.value = songGroup
//    }
}
