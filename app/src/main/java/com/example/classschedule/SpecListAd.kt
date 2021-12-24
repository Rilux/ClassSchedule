package com.example.classschedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classschedule.databinding.SpecListExample1ItemBinding


interface ListenerSpec{
    fun onClick(item: String, item1: String, pos: Int)
}
data class SpecExample(val nameSubject1: String, val teacherID1: String)

class SpecListAd(private val onClickListener: ListenerSpec) : RecyclerView.Adapter<SpecListAd.SpecHolder>(){
    val specList = ArrayList<SpecExample>()

    class SpecHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = SpecListExample1ItemBinding.bind(item)

        fun bind(specess: SpecExample) = with(binding) {
            textSubjectItem.text = specess.nameSubject1
            textTeacherItem.text = specess.teacherID1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecListAd.SpecHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spec_list_example_1_item, parent, false)
        return SpecListAd.SpecHolder(view)
    }

    override fun onBindViewHolder(holder: SpecListAd.SpecHolder, position: Int) {
        holder.bind(specList[position])
        holder.binding.buttonDeleteSpec.setOnClickListener(){
            onClickListener.onClick(holder.binding.textSubjectItem.text.toString(),holder.binding.textTeacherItem.text.toString(), position)
        }
    }

    override fun getItemCount(): Int {
        return specList.size
    }

    fun addSpec(specess: SpecExample) {
        specList.add(specess)
        notifyDataSetChanged()
    }
}