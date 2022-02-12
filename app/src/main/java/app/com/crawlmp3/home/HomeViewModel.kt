    package app.com.crawlmp3.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.com.crawlmp3.network.Album
import app.com.crawlmp3.network.Mp3ApiSearch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class HomeViewModel : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

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
        getMp3Albums()
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}