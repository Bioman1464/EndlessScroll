package com.example.endlessscroll

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.endlessscroll.models.Event

class EventAdapter(var eventList: MutableList<Event>, var context: Context) :
    RecyclerView.Adapter<EventAdapter.EvenViewHolder>() {

    class EvenViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.eventCard)

        val eventTitle = itemView.findViewById<TextView>(R.id.nameEvent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvenViewHolder {
        return EvenViewHolder(LayoutInflater.from(context)
                .inflate(
                    R.layout.item_event,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: EvenViewHolder, position: Int) {
        if(eventList != null) {
            holder.eventTitle.text = eventList[position].id.toString()
        }
    }
}