package com.mistershorr.birthdaytracker

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class BirthdayAdapter(var birthdayList: List<Person>) : RecyclerView.Adapter<BirthdayAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textViewName : TextView
        val textViewDaysUntil: TextView
        val layout : ConstraintLayout
        val checkBoxGiftBought : CheckBox

        init {
            textViewName = view.findViewById(R.id.textView_birthdayItem_name)
            textViewDaysUntil = view.findViewById(R.id.textView_birthdayItem_daysUntil)
            layout = view.findViewById(R.id.layout_birthdayItem)
            checkBoxGiftBought = view.findViewById(R.id.checkBox_birthdayItem_giftBought)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_birthday, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.layout.context
        val person = birthdayList[position]
        holder.textViewName.text = person.name
        var daysDiff = calculateDaysLeft(person)

        holder.textViewDaysUntil.text = "$daysDiff left"
        holder.checkBoxGiftBought.isChecked = person.isPurchased
        holder.layout.setOnClickListener{
            val detailIntent = Intent(context, BirthdayDetailActivity::class.java)
            detailIntent.putExtra(BirthdayDetailActivity.EXTRA_PERSON, person)
            context.startActivity(detailIntent)
        }
    }

    private fun calculateDaysLeft(person: Person): Any {
        val calendarBday = Calendar.getInstance()
        calendarBday.time = person.birthday
        val calendarToday = Calendar.getInstance()
        val bdayMonth = calendarBday.get(Calendar.MONTH)
        val bdayDay = calendarBday.get(Calendar.DAY_OF_MONTH)
        val todayMonth = calendarToday.get(Calendar.MONTH)
        val todayDay = calendarToday.get(Calendar.DAY_OF_MONTH)
        val todayYear = calendarToday.get(Calendar.YEAR)
        if (bdayMonth > todayMonth || bdayMonth == todayMonth && bdayDay > todayDay) {
            calendarBday.set(Calendar.YEAR, todayYear)
        } else if (bdayDay == todayMonth && bdayDay == todayDay) {
            calendarBday.set(Calendar.YEAR, todayYear)
        } else {
            calendarBday.set(Calendar.YEAR, todayYear + 1)
        }
        var difference = calendarBday.timeInMillis - calendarToday.timeInMillis
        var daysDiff = difference / (1000 * 60 * 60 * 24)
        return daysDiff
    }


    override fun getItemCount(): Int {
        return birthdayList.size
    }
}