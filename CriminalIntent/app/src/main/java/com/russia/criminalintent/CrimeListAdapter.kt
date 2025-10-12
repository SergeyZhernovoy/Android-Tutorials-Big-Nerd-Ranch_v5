package com.russia.criminalintent

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.russia.criminalintent.databinding.ListItemCrimeBinding
import com.russia.criminalintent.databinding.ListItemSeriousCrimeBinding
import com.russia.criminalintent.domain.Crime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

class CrimeListAdapter(
    private val crimes: List<Crime>,
    private val onCrimeClicked: (crimeId: UUID) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var holder: RecyclerView.ViewHolder
        when (viewType) {
            0 -> {
                val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
                holder = CrimeHolder(binding)
            }
            1 -> {
                val binding = ListItemSeriousCrimeBinding.inflate(inflater, parent, false)
                holder = SeriousCrimeHolder(binding)
            }
            else -> throw Exception("type")
        }
        return holder
    }

    override fun getItemViewType(position: Int): Int {
        val item = crimes[position]
        return when (item.requiresPolice) {
            true -> 1
            false -> 0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = crimes[position]
        when(item.requiresPolice) {
            true -> {
                (holder as SeriousCrimeHolder).bind(item)
            }
            false -> {
                (holder as CrimeHolder).bind(item, onCrimeClicked)
            }
        }
    }

    override fun getItemCount(): Int {
        return crimes.size
    }

}

class CrimeHolder(
    private val binding: ListItemCrimeBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(crime: Crime, onCrimeClicked: (crimeId: UUID) -> Unit) {
        binding.crimeDate.text = DateFormat.format("EEEEEE, MMMMMM d, yyyy, HH:mm"  ,crime.date)
        binding.crimeTitle.text = crime.title
        binding.root.setOnClickListener {
           onCrimeClicked(crime.id)
        }
        binding.crimeSolved.visibility = if (crime.isSolved) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}

class SeriousCrimeHolder(
    private val binding: ListItemSeriousCrimeBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(crime: Crime) {
        binding.crimeDate.text = crime.date.toString()
        binding.crimeTitle.text = crime.title
        binding.root.setOnClickListener {
            Toast
                .makeText(binding.root.context, "${crime.title} clicked", Toast.LENGTH_SHORT)
                .show()
        }
        binding.callPolice.setOnClickListener {
            Toast
                .makeText(binding.root.context, "This crime is serious!", Toast.LENGTH_SHORT)
                .show()
        }
        binding.crimeSolved.visibility = if (crime.isSolved) {
            View.VISIBLE
        } else {
            View.GONE
        }

    }
}