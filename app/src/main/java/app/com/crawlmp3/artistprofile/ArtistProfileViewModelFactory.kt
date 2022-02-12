package app.com.crawlmp3.artistprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.com.crawlmp3.network.Artist

class ArtistProfileViewModelFactory(
    private val artistProfile: Artist) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ArtistProfileViewModel::class.java))
            return ArtistProfileViewModel(artistProfile) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}