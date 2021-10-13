package com.upax.aplicationupax.vista.movies.movie_list
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.upax.aplicationupax.BuildConfig
import com.upax.aplicationupax.R
import com.upax.aplicationupax.model.Results
import com.upax.aplicationupax.vista.movies.movie_details.DetailMovie
import kotlinx.android.synthetic.main.movie_card.view.*

class AdapterNow (
    private val context: Context,
    private val list: ArrayList<Results>,
    private val listEvent: NotificationEvent

): RecyclerView.Adapter<AdapterNow.listViewHolder>() {

    interface NotificationEvent {
        fun onNotificationTouch(notification: Results)

    }
    inner class listViewHolder(private val view: View): RecyclerView.ViewHolder(view){
        fun onBindItem(list: Results){
            view.titulo.text = list.title
            view.date.text = list.release_date
            Glide.with(context).load(BuildConfig.IMAGE_URL+""+list.poster_path).into(view.imagen)

            view.card_view.setOnClickListener {v ->
                val intent = Intent(v.context, DetailMovie::class.java)
                intent.putExtra("original_language", list.original_language)
                intent.putExtra("original_title", list.original_title)
                intent.putExtra("overview", list.overview)
                intent.putExtra("popularity", list.popularity)
                intent.putExtra("release_date", list.release_date)
                intent.putExtra("vote_average", list.vote_average)
                intent.putExtra("vote_count", list.vote_count)
                intent.putExtra("imagen", BuildConfig.IMAGE_URL+""+list.poster_path)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listViewHolder =
        listViewHolder(LayoutInflater.from(context).inflate(R.layout.movie_card, parent, false))

    override fun onBindViewHolder(holder: listViewHolder, position: Int) =
            holder.onBindItem(list[position])

    override fun getItemCount(): Int =
            list.size

    fun addList(list: List<Results>){
        list.forEach {
            this.list.add(it)
            this@AdapterNow.notifyItemInserted(itemCount - 1)
            System.out.println("it: ||||||"+it)
        }
    }
}