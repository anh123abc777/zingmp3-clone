package app.com.crawlmp3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.com.crawlmp3.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomSheetBehavior : BottomSheetBehavior<NestedScrollView>
    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        val viewModelProvider = MainViewModelFactory(this.application)
        viewModel = ViewModelProvider(this,viewModelProvider).get(MainViewModel::class.java)

        setupNav()

        loadPlaylist()
        playSongSelected()

        createBottomSheet()
        disableSwipedDownToHideBottomSheet()

        setupMiniPlayer()

    }

    private fun loadPlaylist(){
        viewModel.songs.observe(this) {
            Timber.i("complete songs")
            viewModel.onStart()
        }
    }

    private fun playSongSelected(){
        viewModel.songSelected.observe(this) { songSelected ->
            binding.miniPlayer.song = songSelected
            viewModel.showMiniPlayer()
            viewModel.onPlay()
        }
    }

    private fun setupNav(){
        navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.selectedItemId = R.id.homeFragment

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id){
                R.id.playViewFragment -> hideNavBottom()
                else -> showNavBottom()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
            val navController = this.findNavController(R.id.nav_host_fragment)
            return navController.navigateUp()
    }

    private fun showNavBottom(){
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    private fun hideNavBottom(){
        binding.bottomNavigation.visibility = View.GONE
    }

    private fun createBottomSheet(){
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
    }

    private fun disableSwipedDownToHideBottomSheet(){
        bottomSheetBehavior.isHideable = false
    }

    private fun setupMiniPlayer(){
        binding.miniPlayer.viewModel = viewModel
        viewModel.stateBottomSheet.observe(this){
            if(it){
                showBottomSheet()
                viewModel.doneShowBottomSheet()
            }
        }
        setupButtonMiniPlayer()
    }

    private fun showBottomSheet(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setupButtonMiniPlayer(){
        viewModel.stateExoPlayer.observe(this){
            if(it){
                binding.miniPlayer.play.setBackgroundResource(R.drawable.pause)
            }
            else{
                binding.miniPlayer.play.setBackgroundResource(R.drawable.play)
            }
        }
    }
}