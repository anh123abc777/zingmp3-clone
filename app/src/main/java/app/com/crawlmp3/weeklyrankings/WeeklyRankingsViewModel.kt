package app.com.crawlmp3.weeklyrankings

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.com.crawlmp3.network.Album
import app.com.crawlmp3.network.Mp3Api
import app.com.crawlmp3.network.Mp3ApiSearch
import app.com.crawlmp3.network.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class WeeklyRankingsViewModel : ViewModel() {

    private var _status = MutableLiveData<String>()

    val status : LiveData<String>
        get() = _status

    private var _songs = MutableLiveData<List<Song>>()
    val songs : LiveData<List<Song>>
        get() = _songs

    private var _selectedSong =  MutableLiveData<Song>()
    val selectedSong : LiveData<Song>
        get() = _selectedSong

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private var _albums = MutableLiveData<List<Album>>()
    val albums : LiveData<List<Album>>
        get() = _albums

    private var _albumsIndie = MutableLiveData<List<Album>>()
    val albumsIndie : LiveData<List<Album>>
        get() = _albumsIndie

    private var _navigateToTracksOfAlbum = MutableLiveData<String>()
    val navigateToTracksOfAlbum : LiveData<String>
        get() = _navigateToTracksOfAlbum

    fun onClickAlbum(idAlbum: String){
        _navigateToTracksOfAlbum.value = idAlbum
    }

    fun doneNavigatedToTracksOfAlbum(){
        _navigateToTracksOfAlbum.value = ""
    }


    init {
        getMp3Properties()
        getMp3Albums()

    }


    private fun getMp3Properties(){
        coroutineScope.launch(Dispatchers.Main) {
            val getPropertiesDeferred =  Mp3Api.retrofitService.getPropertiesAsync("chart-realtime")
            try {
                _songs.value = getPropertiesDeferred.await().data.song!!
            }
            catch (e : Exception){
                _status.value = "Error : ${e.message}"
            }
        }
    }

    fun onSongClick(song : Song){
        _selectedSong.value = song
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun doneSelect(){
        _selectedSong.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun getMp3Albums(){
        coroutineScope.launch{
            val getAlbumsDeferred = Mp3ApiSearch.retrofitServiceSearch
                .searchKeyAsync("album",10,"Top 100")
            try {
                _albums.value = getAlbumsDeferred.await().data!![0].album!!
                Timber.i(getAlbumsDeferred.await().data!![0].album!![0].name)
            }
            catch (e : Exception){
                Timber.i(e.message)
            }

        }
        coroutineScope.launch{
            val getAlbumsIndieDeferred = Mp3ApiSearch.retrofitServiceSearch
                .searchKeyAsync("album",10,"Indie")
            try {
                _albumsIndie.value = getAlbumsIndieDeferred.await().data!![0].album!!
                Timber.i(getAlbumsIndieDeferred.await().data!![0].album!![0].name)
                Timber.i(_albums.value!![0].name)
            }
            catch (e : Exception){
                Timber.i(e.message)
            }
        }
    }

}

