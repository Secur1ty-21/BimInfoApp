package ru.yamost.bininfo.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.yamost.bininfo.databinding.ItemSearchHistoryBinding

interface SearchHistoryListener {
    fun onItemClickListener(cardBin: String)
}

class SearchHistoryAdapter(
    private val searchHistoryListener: SearchHistoryListener
) : ListAdapter<String, SearchHistoryAdapter.SearchHistoryViewHolder>(DiffCallBack),
    View.OnClickListener {

    class SearchHistoryViewHolder(
        val binding: ItemSearchHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchHistoryBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return SearchHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        with(holder.binding) {
            cardBin.text = getItem(position)
        }
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<String>() {
            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem === newItem
            }
        }
    }

    override fun onClick(v: View) {
        val textView = v as TextView
        searchHistoryListener.onItemClickListener(textView.text.toString())
    }
}