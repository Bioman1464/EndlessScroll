package com.example.endlessscroll.ui.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.endlessscroll.models.Message

class MessageAdapter (var messages: ArrayList<Message>, var context: Context) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        return messages.size
    }

    override fun getItem(position: Int): Any {
        return messages[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun add(message: Message) {
        this.messages.add(message)
        notifyDataSetChanged()
    }

}

class MessagesViewHolder (
    var avatar: View,
    var name: TextView,
    var messageBody: TextView
)