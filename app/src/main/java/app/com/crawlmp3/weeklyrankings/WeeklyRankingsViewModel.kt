package app.com.crawlmp3.weeklyrankings

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.com.crawlmp3.network.Mp3Api
import app.com.crawlmp3.network.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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


    init {
        getMp3Properties()
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

}

