package com.example.classschedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classschedule.databinding.TaughtstatitemBinding

interface ListenertaughtStat {
    fun onClick(item: String, item1: String, pos: Int)
}

data class StatTauExample(
    val nameClassTaughtStat: String,
    val nameSubjTaughtStat: String,
    val hoursTaughtStat: Int
)

class TaughtStatList(private val onClickListener: ListenertaughtStat) :
    RecyclerView.Adapter<TaughtStatList.TauStatHolder>() {

    val taustatList = ArrayList<StatTauExample>()

    class TauStatHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = TaughtstatitemBinding.bind(item)

        fun bind(specess: StatTauExample) = with(binding) {
            textClasstaughtStat.text = specess.nameClassTaughtStat
            textTaughtSub.text = specess.nameSubjTaughtStat
            textTaughtStatHours.text = specess.hoursTaughtStat.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaughtStatList.TauStatHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.taughtstatitem, parent, false)
        return TaughtStatList.TauStatHolder(view)
    }

    override fun onBindViewHolder(holder: TaughtStatList.TauStatHolder, position: Int) {
        holder.bind(taustatList[position])
/*        holder.binding.buttonDeleteSpec.setOnClickListener(){

        }*/
    }

    override fun getItemCount(): Int {
        return taustatList.size
    }

    fun addTauStat(specess: StatTauExample) {
        taustatList.add(specess)
        notifyDataSetChanged()
    }

}