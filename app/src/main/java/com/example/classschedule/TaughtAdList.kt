package com.example.classschedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classschedule.databinding.TaughtItmeBinding


interface ListenerTaught {
    fun onClick(item: String, item1: String, item3: String, item4: String, pos: Int, state: Int)
}

data class TauhgtExample(
    val nameClassTaught1: String,
    val nameSubjectTaught1: String,
    val nameTeacher1: String,
    val nameHoursTaught: String
)

class TaughtAdList(private val onClickListener: ListenerTaught) :
    RecyclerView.Adapter<TaughtAdList.TaughtHolder>() {
    val taughtList = ArrayList<TauhgtExample>()

    class TaughtHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = TaughtItmeBinding.bind(item)

        fun bind(specess: TauhgtExample) = with(binding) {
            textIDClassTaugth.text = specess.nameClassTaught1
            textNameSubjectTaugth.text = specess.nameSubjectTaught1
            textIDTeacherTaugth.text = specess.nameTeacher1
            texthoursTaugth.text = specess.nameHoursTaught
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaughtAdList.TaughtHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.taught_itme, parent, false)
        return TaughtAdList.TaughtHolder(view)
    }

    override fun onBindViewHolder(holder: TaughtAdList.TaughtHolder, position: Int) {
        holder.bind(taughtList[position])
        holder.binding.buttonRedactTaught.setOnClickListener() {
            onClickListener.onClick(
                holder.binding.textIDClassTaugth.text.toString(),
                holder.binding.textNameSubjectTaugth.text.toString(),
                holder.binding.textIDTeacherTaugth.text.toString(),
                holder.binding.texthoursTaugth.text.toString(),
                position,
                1
            )
        }
        holder.binding.buttonDeleteTaught.setOnClickListener() {
            onClickListener.onClick(
                holder.binding.textIDClassTaugth.text.toString(),
                holder.binding.textNameSubjectTaugth.text.toString(),
                holder.binding.textIDTeacherTaugth.text.toString(),
                holder.binding.texthoursTaugth.text.toString(),
                position,
                2
            )
        }
    }

    override fun getItemCount(): Int {
        return taughtList.size
    }

    fun addTaught(specess: TauhgtExample) {
        taughtList.add(specess)
        notifyDataSetChanged()
    }
}