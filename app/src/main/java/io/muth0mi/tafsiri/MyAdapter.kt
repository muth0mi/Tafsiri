package io.muth0mi.tafsiri


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.muth0mi.tafsiri.databinding.WordRowBinding

class MyAdapter(val activity: Activity) : RecyclerView.Adapter<MyAdapter.Row>() {

    // Variables
    private var words = ArrayList<Dictionary>()

    fun setWords(words: ArrayList<Dictionary>) {
        this.words = words
    }

    // Return size of recyclerView
    override fun getItemCount(): Int {
        return words.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Row {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.word_row, parent, false)
        return Row(itemView)
    }

    // Method to handle widget events
    override fun onBindViewHolder(holder: Row, position: Int) {
        val word = words[position]

        holder.bind((position + 1).toString() + ". " + word.swahiliWord) // Bind recyclerView row

    }


    inner class Row(view: View) : RecyclerView.ViewHolder(view) {


        private val binding: WordRowBinding = DataBindingUtil.bind(view)!!

        fun bind(word: String) {
            binding.word = word
        }

    }

}