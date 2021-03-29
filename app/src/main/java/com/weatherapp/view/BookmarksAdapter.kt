package com.weatherapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.weatherapp.R
import com.weatherapp.WeatherViewModel
import com.weatherapp.model.valueobject.Bookmark

/**
 * Created by Shiva Kishore on 3/8/2021.
 */
class BookmarksAdapter(val viewModel: WeatherViewModel) :
    RecyclerView.Adapter<BookmarksAdapter.BookMarkViewHolder>() {
    private val bookmarks = ArrayList<Bookmark>()

    inner class BookMarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cityName: TextView = itemView.findViewById(R.id.cityname)
        var im_close: ImageButton = itemView.findViewById(R.id.im_close)
        var item_bookmark: ConstraintLayout = itemView.findViewById(R.id.item_bookmark)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkViewHolder {
        return BookMarkViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return bookmarks.size
    }

    override fun onBindViewHolder(holder: BookMarkViewHolder, position: Int) {
        holder.cityName.text = bookmarks[position].name
        holder.im_close.setOnClickListener {
            removeBookmark(position)
        }
        holder.item_bookmark.setOnClickListener {
            viewModel.detailedViewClick.value = bookmarks[position]
        }
    }

    private fun removeBookmark(position: Int) {
        viewModel.removeBookmark(bookmarks[position])
        bookmarks.removeAt(position)
        notifyDataSetChanged()
    }

    fun setBookmarks(bookmarkList: List<Bookmark>) {
        bookmarks.clear()
        bookmarks.addAll(bookmarkList)
        notifyDataSetChanged()
    }

    fun addBookmark(bookmark: Bookmark) {
        bookmarks.add(bookmark)
        notifyItemInserted(bookmarks.size)
    }
}