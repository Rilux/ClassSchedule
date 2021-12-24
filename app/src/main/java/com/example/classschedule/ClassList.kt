package com.example.classschedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classschedule.databinding.ClassItemBinding

interface Listener{
    fun onClick(item: String)
}

class ClassList(private val onClickListener: Listener) : RecyclerView.Adapter<ClassList.ClassHolder>(){
    val classList = ArrayList<ClassExample>()

    class ClassHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ClassItemBinding.bind(item)

        fun bind(classes: ClassExample) = with(binding) {
            className123.text = classes.className
            classTeacherName123.text = classes.classTeacherName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.class_item, parent, false)
        return ClassHolder(view)
    }

    override fun onBindViewHolder(holder: ClassHolder, position: Int) {
        holder.bind(classList[position])
        holder.binding.className123.setOnClickListener(){
            onClickListener.onClick(holder.binding.className123.text.toString())
        }
    }

    override fun getItemCount(): Int {
        return classList.size
    }

    fun addClass(classes: ClassExample) {
        classList.add(classes)
        notifyDataSetChanged()
    }

}