package app.com.crawlmp3.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlaylistViewModelFactory(
    private val idAlbum : String?=null) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PlaylistViewModel::class.java)){
            return PlaylistViewModel(idAlbum) as T
        }
        throw IllegalArgumentException ("Unknown ViewModel class")
    }
}