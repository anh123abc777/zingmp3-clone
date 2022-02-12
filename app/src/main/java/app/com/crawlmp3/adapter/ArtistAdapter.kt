package app.com.crawlmp3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.com.crawlmp3.databinding.ArtistElementViewBinding
import app.com.crawlmp3.network.Artist


class ArtistAdapter(private val clickListener : OnClickListener) : ListAdapter<Artist, ArtistAdapter.ViewHolder>(DiffCallBack) {

    companion object DiffCallBack : DiffUtil.ItemCallback<Artist>() {
        override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem===newItem
        }

        override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem.id==newItem.id
        }

    }

    class ViewHolder private constructor(
        private val binding : ArtistElementViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(artist: Artist?, clickListener: OnClickListener){
            binding.artist = artist
            binding.clickListener = clickListener
        }
       companion object{
           fun from(parent: ViewGroup) : ViewHolder{
               val layoutInflater = LayoutInflater.from(parent.context)
               val binding = ArtistElementViewBinding.inflate(layoutInflater,parent,false)
               return ViewHolder(binding)
           }
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newItem = getItem(position)
        holder.bind(newItem,clickListener)
    }
}

class OnClickListener (val clickListener : (artist: Artist) -> Unit){
    fun onClick(artist : Artist) = clickListener(artist)
}
