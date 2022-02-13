package app.com.crawlmp3.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import app.com.crawlmp3.network.Album

class PlaylistAdapter(
    private val clickListener: OnClickListener) : ListAdapter<Album, PlaylistAdapter.ViewHolder>(DiffCallBack){
    companion object DiffCallBack : DiffUtil.ItemCallback<Album>() {
        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem===newItem
        }

    }

    class ViewHolder private constructor(
        val binding : app.com.crawlmp3.databinding.ItemPlaylistBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album?, clickListener: OnClickListener) {
            binding.album = album
            binding.onListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = app.com.crawlmp3.databinding.ItemPlaylistBinding
                                .inflate(layoutInflater,parent,false)

                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = getItem(position)
        holder.bind(album,clickListener)
    }
}

class OnClickListener(val clickListener: (idAlbum : String) -> Unit){
    fun onClick(albumId : String) = clickListener(albumId)
}
