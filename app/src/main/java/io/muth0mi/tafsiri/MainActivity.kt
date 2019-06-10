package io.muth0mi.tafsiri

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.muth0mi.tafsiri.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main) // Bind layout

        // Hide Content by default
        binding.clear.isEnabled = false
        binding.exit.isEnabled = false
        binding.progressBar.visibility = View.GONE

        // Set Up recyclerView Adapter
        val adapter = MyAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        // Edit text Listener
        binding.et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                binding.clear.isEnabled = s.isNotEmpty()
                binding.exit.isEnabled = s.isNotEmpty()

                binding.rvHeader.text = ""
                binding.recyclerView.visibility = View.GONE
            }
        })

        // Look Up
        binding.exit.setOnClickListener {
            binding.exit.isEnabled = false
            populateRv(adapter, binding.et.text.toString(), binding)
        }

        // Clear
        binding.clear.setOnClickListener { binding.et.text = null }

    }

    private fun populateRv(adapter: MyAdapter, word: String, binding: ActivityMainBinding) {
        binding.progressBar.visibility = View.VISIBLE
        binding.et.isEnabled = false
        binding.clear.isEnabled = false

        Thread(Runnable {
            // Read values from CSV
            val inputStream = resources.openRawResource(R.raw.data)
            val dictionary = read(inputStream)

            runOnUiThread {
                binding.progressBar.visibility = View.GONE
                binding.et.isEnabled = true
                binding.clear.isEnabled = true

                // Get translations for word
                val translations = ArrayList<Dictionary>()
                for (i in 0 until dictionary.size) {
                    if (dictionary[i].englishWord == word) translations.add(dictionary[i])
                }

                if (translations.isEmpty()) binding.rvHeader.text = "No translations available for " + binding.et.text.toString()
                else binding.rvHeader.text = "Translations for " + binding.et.text.toString()

                // Populate RecyclerView
                binding.recyclerView.visibility = View.VISIBLE
                adapter.setWords(translations)
                adapter.notifyDataSetChanged()
            }
        }).start()
    }

    private fun read(inputStream: InputStream): ArrayList<Dictionary> {
        // ArrayList to populate
        val resultList = ArrayList<Dictionary>()

        val reader = BufferedReader(InputStreamReader(inputStream))
        try {
            while ((reader.readLine()) != null) {
                val csvLine: String = reader.readLine()

                val row = csvLine.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                val dictionary = Dictionary()
                dictionary.swahiliWord = row[0]
                dictionary.englishWord = row[1]

                resultList.add(dictionary)
            }
        } catch (ignored: IOException) {
        }

        inputStream.close()

        return resultList
    }

}