package app.com.crawlmp3.search

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.com.crawlmp3.function.Crawling
import app.com.crawlmp3.function.DataCrawler
import app.com.crawlmp3.network.Artist
import app.com.crawlmp3.network.Mp3ApiSearch
import app.com.crawlmp3.network.RawSearch
import app.com.crawlmp3.network.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchViewModel : ViewModel(){

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _rawSearch = MutableLiveData<RawSearch>()
    private var _query = MutableLiveData<String>()
    private var filter = FilterHolder()

    val rawSearch : LiveData<RawSearch>
        get() = _rawSearch

    val query : LiveData<String>
        get() = _query

    fun setQuery(query : String){
        _query.value = query
    }

    fun onQueryChanged(){
        coroutineScope.launch {
            val getResultDeferred = Mp3ApiSearch.retrofitServiceSearch
                .searchKeyAsync("song,album,top,artist",100,_query.value!!)
            _rawSearch.value = getResultDeferred.await()
            Timber.i("complete search")
        }
    }

    private var _navigateArtist = MutableLiveData<Artist>()
    val navigateArtist : LiveData<Artist>
        get() = _navigateArtist

    fun onClickArtist(artist: Artist){
        _navigateArtist.value = artist
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun doneNavigatedToArtistProfile(){
        _navigateArtist.value = null
    }


    private class FilterHolder{
        var currentValue : String?= null
            private set
        fun update(changedFilter: String, isChecked: Boolean):Boolean{
            if(isChecked){
                currentValue = changedFilter
                return true
            } else if(currentValue==changedFilter){
                currentValue="album,song,top,artist"
                return true
            }
            return false
        }
    }

//    private var _songs = MutableLiveData<List<Song>>()
//    val songs : LiveData<List<Song>>
//        get() = _songs

    private var _songSelected =  MutableLiveData<Song>()
    val songSelected : LiveData<Song>
        get() = _songSelected

    fun onSongClick(song : Song){
        _songSelected.value = song
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun donePlayedSong(){
        _songSelected.value = null
    }

    private var _navigateToTracksOfAlbum = MutableLiveData<String>()
    val navigateToTracksOfAlbum : LiveData<String>
        get() = _navigateToTracksOfAlbum

    fun onClickAlbum(idAlbum: String){
        _navigateToTracksOfAlbum.value = idAlbum
    }

    fun doneNavigatedToTracksOfAlbum(){
        _navigateToTracksOfAlbum.value = ""
    }


}