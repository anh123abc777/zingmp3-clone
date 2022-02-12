package app.com.crawlmp3.play

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.com.crawlmp3.network.Song
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.coroutines.*
import timber.log.Timber



enum class ACTIONS{
    NEXT, PLAY, PAUSE, PREVIOUS, PLAYING, ONPAUSE
}

class PlayViewViewModel : ViewModel() {

    private var _action = MutableLiveData<ACTIONS>()
    val action : LiveData<ACTIONS>
        get() = _action

    private var _song = MutableLiveData<Song>()
    val song : LiveData<Song>
        get() = _song

    var exoPlayer: SimpleExoPlayer? = null

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _progress = MutableLiveData<Int>()
    val progress : LiveData<Int>
        get() = _progress

    init {
        _progress.value = 0
    }

    fun setData(song : Song){
        _song.value = song
    }

    fun onNext(){
        _action.value = ACTIONS.NEXT
        Timber.i("Next")
    }

    fun onPrevious(){
        _action.value = ACTIONS.PREVIOUS
        Timber.i("Previous")
    }

    fun onChangeState(){
       if (_action.value==ACTIONS.PLAYING)  onPause() else onPlay()
    }

    private fun onPlay(){
        _action.value = ACTIONS.PLAY
        Timber.i("Play")
    }

    fun setStatePlaying(){
        _action.value = ACTIONS.PLAYING
    }

    fun setStateOnPause(){
        _action.value = ACTIONS.ONPAUSE
    }

    private fun onPause(){
        _action.value = ACTIONS.PAUSE
        Timber.i("pause")
    }

    fun doneAction(){
        _action.value = if (_action.value==ACTIONS.PLAY)  ACTIONS.PLAYING else ACTIONS.ONPAUSE
    }

    fun initSeekBar()=
        coroutineScope.launch {
            Timber.i("init SeekBar")
            var currentPosition = 0
            while (currentPosition!= _song.value!!.duration){
                if(exoPlayer==null) {
                    delay(1000L)
                }else{
                    currentPosition = exoPlayer!!.currentPosition.toInt().div(1000)
                    _progress.value = currentPosition
                    delay(500L)
                }
            }
        }


    fun seekTo(progress : Int){
        _progress.value = progress
        exoPlayer?.seekTo(progress.toLong()*1000)
    }


    override fun onCleared() {
        super.onCleared()
        exoPlayer?.release()
        Timber.i("release")
        viewModelJob.cancel()
    }
}