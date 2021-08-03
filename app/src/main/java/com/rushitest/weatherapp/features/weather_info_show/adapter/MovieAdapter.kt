package com.rushitest.weatherapp.features.weather_info_show.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rushitest.weatherapp.R
import com.rushitest.weatherapp.features.weather_info_show.model.data_class.Movie

class MovieAdapter() : RecyclerView.Adapter<MovieViewHolder>() {

    var movies = mutableListOf<Movie>()

    /** Getting data from RecyclerActivity in to movies
     * */
    fun setMovieList(movies: List<Movie>) {
        this.movies = movies.toMutableList()
        notifyDataSetChanged()
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflatedView = parent.inflate(R.layout.adapter_movie_item, false)
        return MovieViewHolder(inflatedView)

    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position] // extracting data from movies list
        holder.movieName.text = movie.name
        Glide.with(holder.itemView.context).load(movie.imageUrl).into(holder.moviePoster)

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "you click ${movie.name}", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return movies.size
    }
}

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val moviePoster = itemView.findViewById(R.id.moviePoster) as ImageView
    val movieName = itemView.findViewById(R.id.movieName) as TextView
}