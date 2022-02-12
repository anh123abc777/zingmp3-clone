package app.com.crawlmp3.function

import app.com.crawlmp3.network.Mp3Api
import app.com.crawlmp3.network.Mp3ApiViewSource
import app.com.crawlmp3.network.Raw
import app.com.crawlmp3.network.Song
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.lang.Exception

class DataCrawler(private var key: String, private val mission : Crawling) {

    private var sourceHtmlAlbum = String()
    private var songs = listOf<Song>()

    fun getTracks() : List<Song>{
        if(key!="") {
            createEndWithHTMLForIdAlbum()
            getSourceHtmlAlbum()
            getKeyAlbum()
            getJSONTracks()
        }
        return songs
    }

    private fun createEndWithHTMLForIdAlbum(){
        key += ".html"
    }

    private fun getSourceHtmlAlbum() = runBlocking {
        try {
            var getSourceHtmlAlbumDeferred : Deferred<String>
            if(mission == Crawling.Album) {
                getSourceHtmlAlbumDeferred = Mp3ApiViewSource.retrofitServiceViewSource
                    .getKeyAsync(key)
            }else{
                getSourceHtmlAlbumDeferred = Mp3ApiViewSource.retrofitServiceViewSource
                    .getKeyAsync(key,"song_artist_play")
            }
            Timber.i("getDeferred completed")
            sourceHtmlAlbum = getSourceHtmlAlbumDeferred.await()
            Timber.i("getSourceString completed")
        }
        catch (e : Exception){
            Timber.i("error Html ${e.message}")
        }
    }

    private fun getKeyAlbum(){
        try {
            sourceHtmlAlbum = sourceHtmlAlbum.substringAfter("data-xml=\"")
            sourceHtmlAlbum = sourceHtmlAlbum.substringBefore("\"")
            sourceHtmlAlbum = sourceHtmlAlbum.substringAfter("key=")
            if(mission==Crawling.Artist){
                sourceHtmlAlbum = sourceHtmlAlbum.substringBefore("&")
            }
            Timber.i(sourceHtmlAlbum)
        }
        catch (e : Exception){
            Timber.i(e.message)
        }
    }

    private fun getJSONTracks() = runBlocking {
        try {
            val getTracksDeferred : Deferred<Raw>
            if(mission==Crawling.Artist){
                getTracksDeferred = Mp3Api.retrofitService.getPropertiesAsync(
                    "media", "get-source", "playlist", sourceHtmlAlbum,
                    "song_artist_play"
                )
            }else {
                 getTracksDeferred = Mp3Api.retrofitService.getPropertiesAsync(
                    "media", "get-source", "album", sourceHtmlAlbum
                )
            }
            songs = getTracksDeferred.await().data.song!!
        }
        catch (e : Exception){
            Timber.i("error getJSONTracks ${e.message}")
        }
    }
}