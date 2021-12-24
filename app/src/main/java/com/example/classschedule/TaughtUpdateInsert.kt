package com.example.classschedule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classschedule.databinding.ActivityTaughtUpdateInsertBinding
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class TaughtUpdateInsert : AppCompatActivity() {

    val classes: MutableList<String?> = ArrayList()
    val subject: MutableList<String?> = ArrayList()
    val teachers: MutableList<String?> = ArrayList()
    val teachersID: MutableList<String?> = ArrayList()
    val hoursArr1: MutableList<String?> = ArrayList()
    var hour1: Int = 0
    var teachersID1: Int = 0
    var state: Int = 0

    lateinit var binding1: ActivityTaughtUpdateInsertBinding
    lateinit var nameClass: String
    lateinit var nameSub1: String
    lateinit var teacherName1: String
    lateinit var hoursName1: String


    private val adapter = TaughtAdList(object : ListenerTaught {
        override fun onClick(
            tag: String,
            tag1: String,
            tag2: String,
            tag3: String,
            pos: Int,
            st: Int
        ) {
            nameClass = tag
            nameSub1 = tag1
            teacherName1 = tag2
            hoursName1 = tag3
            teachersID1 = 4
            when (st) {
                1 -> startNewActivityUpdateTaught()
                2 -> execute12("Delete Taught where ID_Class = '$tag' and Name_Subject = '$nameSub1'")
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding1 = ActivityTaughtUpdateInsertBinding.inflate(layoutInflater)
        setContentView(binding1.root)
        supportActionBar?.hide()

        execute23()
        init1()
    }

    fun init1() {
        binding1.apply {
            RcViewTaught.layoutManager = LinearLayoutManager(this@TaughtUpdateInsert)
            RcViewTaught.adapter = adapter
            for (i in classes.indices) {
                val class1 = TauhgtExample(
                    classes[i].toString(),
                    subject[i].toString(),
                    teachers[i].toString(),
                    hoursArr1[i].toString()
                )
                adapter.addTaught(class1)
            }
        }
    }

    private fun execute23() {
        var statement: Statement? = null

        try {
            var myCon = MainActivity.connectionClass()?.dbConn()
            statement = myCon!!.createStatement()

            var resultSet: ResultSet =
                statement.executeQuery("Select ID_Class, Name_Subject, ID_Teacher, Hours, (Select Full_Name from Teacher where Taught.ID_Teacher = Teacher.ID_Teacher) as Full_Name From Taught;")

            while (resultSet!!.next()) {
                classes.add(resultSet.getString("ID_Class"))
                teachers.add(resultSet.getString("Full_Name"))
                teachersID.add(resultSet.getString("ID_Teacher"))
                subject.add(resultSet.getString("Name_Subject"))
                hoursArr1.add(resultSet.getString("Hours"))
            }
        } catch (e: SQLException) {
            Log.e("Error:", e.message.toString())
        }
    }

    private fun execute12(query: String) {
        var statement: Statement? = null

        try {
            var myCon = MainActivity.connectionClass()?.dbConn()
            statement = myCon!!.createStatement()
            statement.execute(query)

        } catch (e: SQLException) {
            Log.e("Error:", e.message.toString())
        }
    }

    private fun startNewActivityUpdateTaught() {
        val intent1 = Intent(this, TaughtUpdateActivity::class.java)
        val temp1: String = teachersID1.toString()
        intent1.putExtra("nameClassUpdateTaught", nameClass)
        intent1.putExtra("nameSubjectUpdateTaught", nameSub1)
        intent1.putExtra("teacherNameUpdateTaught", teacherName1)
        intent1.putExtra("hoursNameUpdateTaught", hoursName1)
        intent1.putExtra("teachersIDUpdateTaught", temp1)

        startActivity(intent1)
    }
}