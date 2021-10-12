package com.upax.aplicationupax.vista.movies.movie_details
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.upax.aplicationupax.R
import com.upax.aplicationupax.utils.LoadingMessage
import kotlinx.android.synthetic.main.movie_details.*


class DetailMovie : LoadingMessage() {

    private lateinit var viewModel: DetailMovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_details)

        viewModel = ViewModelProvider(this)[DetailMovieViewModel::class.java]

        val bundle: Bundle? = intent.extras

        val original_languageV = bundle?.get("original_language")
        val original_titleV = bundle?.get("original_title")
        val overviewV = bundle?.get("overview")
        val popularityV = bundle?.get("popularity")
        val release_dateV = bundle?.get("release_date")
        val vote_averageV = bundle?.get("vote_average")
        val vote_countV = bundle?.get("vote_count")
        val imagen = bundle?.get("imagen")

        Glide.with(this@DetailMovie).load(imagen).into(imagen_details)
        original_title.text = original_titleV.toString()
        original_language.text = "Original Lenguaje: "+original_languageV.toString()
        popularity.text = "Popularity: "+popularityV.toString()
        release_date.text = "Release date : "+release_dateV.toString()
        vote_average.text = "Vote average: "+vote_averageV.toString()
        vote_count.text = "Vote count: "+vote_countV.toString()
        overview.text = "Overview: "+overviewV.toString()

        val youTubePlayerView: YouTubePlayerView = findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = "qv8eF1u0-QU"
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
    }

}
