package com.example.classschedule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classschedule.databinding.ActivitySpecUpdateDeleteBinding
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class SpecUpdateDeleteActivity : AppCompatActivity() {
    val subject: MutableList<String?> = ArrayList()
    val teachers: MutableList<String?> = ArrayList()
    val teachersID: MutableList<String?> = ArrayList()
    lateinit var binding1: ActivitySpecUpdateDeleteBinding

    lateinit var nameSub1: String
    lateinit var teacherName1: String
    var teachersID1: Int = 0


    private val adapter = SpecListAd(object : ListenerSpec {
        override fun onClick(tag: String, tag1: String, pos: Int) {

            nameSub1 = tag
            teacherName1 = tag1
            teachersID1 = pos
            execute1("Delete Specialization where ID_Teacher = '${pos + 1}' and Name_Subject = '$tag'")
        }
    })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding1 = ActivitySpecUpdateDeleteBinding.inflate(layoutInflater)
        setContentView(binding1.root)
        supportActionBar?.hide()
        execute()
        init1()

    }


    private fun init1() {
        binding1.apply {
            specRcView.layoutManager = LinearLayoutManager(this@SpecUpdateDeleteActivity)
            specRcView.adapter = adapter

            for (i in subject.indices) {
                val class1 = SpecExample(subject[i].toString(), teachers[i].toString())
                adapter.addSpec(class1)
            }
            adapter.notifyDataSetChanged()
        }
    }

    fun execute() {
        var statement: Statement? = null

        try {
            var myCon = MainActivity.connectionClass()?.dbConn()
            statement = myCon!!.createStatement()

            var resultSet: ResultSet =
                statement.executeQuery("Select s1.ID_Teacher, s1.Name_Subject, Full_Name From Specialization s1 inner join Teacher on s1.ID_Teacher = Teacher.ID_Teacher;")
            while (resultSet!!.next()) {
                teachers.add(resultSet.getString("Full_Name"))
                teachersID.add(resultSet.getString("ID_Teacher"))
                subject.add(resultSet.getString("Name_Subject"))
            }
        } catch (e: SQLException) {
            Log.e("Error:", e.message.toString())
        }
    }
    fun execute1(query: String) {
        var statement: Statement? = null

        try {
            var myCon = MainActivity.connectionClass()?.dbConn()
            statement = myCon!!.createStatement()
            statement.execute(query)

        } catch (e: SQLException) {
            Log.e("Error:", e.message.toString())
        }
    }
}