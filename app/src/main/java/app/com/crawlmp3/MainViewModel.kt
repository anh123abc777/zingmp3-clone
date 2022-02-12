package app.com.crawlmp3

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.com.crawlmp3.network.Song
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import kotlinx.coroutines.*
import timber.log.Timber

class MainViewModel(var application: Application)
 : ViewModel() {

    private var _songs = MutableLiveData<List<Song>>()
    val songs : LiveData<List<Song>>
        get() = _songs

    private var _songSelected = MutableLiveData<Song>()
    val songSelected : LiveData<Song>
        get() = _songSelected

    private var _isShowMiniPlayer = MutableLiveData<Boolean>()
    val isShowMiniPlayer : LiveData<Boolean>
        get() = _isShowMiniPlayer

    private var _stateBottomSheet = MutableLiveData<Boolean>()
    val stateBottomSheet : LiveData<Boolean>
        get() = _stateBottomSheet

    private var _stateExoPlayer = MutableLiveData<Boolean>()
    val stateExoPlayer : LiveData<Boolean>
        get() = _stateExoPlayer

    var exoPlayer: SimpleExoPlayer? = null
    private val viewModelJob = Job()

    init {
        initExoPlayer()
        _stateExoPlayer.value = false

        _isShowMiniPlayer.value = false
    }

    fun setData(songGroup : List<Song>){
        _songs.value = songGroup
    }

    fun setSongSelected(song: Song){
        _songSelected.value = song
    }

    fun showBottomSheet(){
        _stateBottomSheet.value = true
    }

    fun doneShowBottomSheet(){
        _stateBottomSheet.value = false
    }

    fun showMiniPlayer(){
        _isShowMiniPlayer.value = true
    }

    private fun initExoPlayer() {
            val extractorsFactory = DefaultExtractorsFactory()
                .setTsExtractorFlags(DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES)
                .setTsExtractorFlags(DefaultTsPayloadReaderFactory.FLAG_DETECT_ACCESS_UNITS)
            exoPlayer = SimpleExoPlayer.Builder(application.applicationContext!!)
                .setMediaSourceFactory(
                    DefaultMediaSourceFactory(
                        application.applicationContext,
                        extractorsFactory
                    )
                )
                .build()
            Timber.i("init ExoPlayer")

    }

    fun onStart() = runBlocking{
        exoPlayer!!.clearMediaItems()
        launch {
            _songs.value!!.forEach { song ->
                exoPlayer!!.addMediaItem(
                    MediaItem.fromUri(
                        application.getString(
                            R.string.getUrlMp3,
                            song.id
                        )
                    )
                )
            }
            Timber.i("on Init")
        }
        delay(600L)
    }


    fun onPlay()  {
        onStop() // stop current song
        exoPlayer?.seekTo(posSongSelectedInGroup(),0L)
        exoPlayer?.prepare()
        Timber.i("on prepare")
        exoPlayer?.play()
        _stateExoPlayer.value = true
        Timber.i("onPlay")
    }

    private fun onContinue(){
        exoPlayer?.play()
        _stateExoPlayer.value = true
    }

    private fun onStop(){
        if(exoPlayer!=null){
            exoPlayer?.stop()
            Timber.i("on Stop")
        }
    }

    private fun onPause(){
        exoPlayer?.pause()
        _stateExoPlayer.value = false
    }

    fun onChangeStatePlay(){
        if(exoPlayer?.isPlaying!!){
            onPause()
        } else{
            onContinue()
        }
    }

    fun onNext(){
        if(exoPlayer!!.hasNextWindow()){
            exoPlayer?.seekTo(exoPlayer!!.currentWindowIndex + 1, 0L)
            setSongSelected(_songs.value!![posSongSelectedInGroup()+1])
            _stateExoPlayer.value = true
        }
    }

    fun onPrevious(){
        if (exoPlayer!!.hasPreviousWindow()) {
            exoPlayer?.seekTo(exoPlayer!!.currentWindowIndex - 1, 0L)
            setSongSelected(_songs.value!![posSongSelectedInGroup() - 1])
            _stateExoPlayer.value = true
        }
    }

     private fun posSongSelectedInGroup() = songs.value!!.indexOfFirst {
        it.id == _songSelected.value!!.id
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer?.release()
        viewModelJob.cancel()
    }
}