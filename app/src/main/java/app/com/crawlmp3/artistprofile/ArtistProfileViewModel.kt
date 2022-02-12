package app.com.crawlmp3.artistprofile

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.com.crawlmp3.function.Crawling
import app.com.crawlmp3.function.DataCrawler
import app.com.crawlmp3.network.Artist
import app.com.crawlmp3.network.Song

class ArtistProfileViewModel(val artistProfile: Artist) : ViewModel() {

    private var _songs = MutableLiveData<List<Song>>()
    val songs : LiveData<List<Song>>
        get() = _songs

    private val dataCrawler = DataCrawler(artistProfile.id!!,Crawling.Artist)

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

    init {
        _songs.value = dataCrawler.getTracks()
    }

}