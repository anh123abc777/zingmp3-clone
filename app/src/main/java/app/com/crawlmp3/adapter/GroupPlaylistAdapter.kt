package app.com.crawlmp3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.com.crawlmp3.adapter.GroupPlaylistAdapter.ViewHolder
import app.com.crawlmp3.databinding.ItemGroupPlaylistBinding
import app.com.crawlmp3.home.OnClickListener
import app.com.crawlmp3.home.PlaylistAdapter
import app.com.crawlmp3.network.GroupPlaylist

class GroupPlaylistAdapter(private val clickListener: app.com.crawlmp3.home.OnClickListener): ListAdapter<GroupPlaylist, ViewHolder>(DiffCallBack) {

    companion object DiffCallBack: DiffUtil.ItemCallback<GroupPlaylist>() {
        override fun areItemsTheSame(oldItem: GroupPlaylist, newItem: GroupPlaylist): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: GroupPlaylist, newItem: GroupPlaylist): Boolean {
            return oldItem.title == newItem.title
        }

    }

    class ViewHolder private
    constructor(val binding: ItemGroupPlaylistBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(groupPlaylist: GroupPlaylist, clickListener: OnClickListener){
            binding.groupPlaylist = groupPlaylist
            val playlistAdapter = PlaylistAdapter(app.com.crawlmp3.home.OnClickListener {
                clickListener.onClick(it)
            })
            binding.playlist.adapter = playlistAdapter
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemGroupPlaylistBinding.inflate(layoutInflater,parent,false)

                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),clickListener)
    }
}