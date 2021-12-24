package com.example.classschedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classschedule.databinding.ActivityStartBinding
import com.example.classschedule.databinding.ActivityStathoursBinding
import java.lang.Exception
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class StathoursActivity : AppCompatActivity() {
    val classes: MutableList<String?> = ArrayList()
    val subject: MutableList<String?> = ArrayList()
    lateinit var binding: ActivityStathoursBinding

    val hours: MutableList<Int?> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStathoursBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        execute(
            "Select ID_Class, Name_Subject, count(*) as hours from Held group by ID_Class, Name_Subject",
            1
        )
        init123()
    }

    private val adapter = TaughtStatList(object : ListenertaughtStat {
        override fun onClick(tag: String, item1: String, pos: Int) {

        }
    })


    private fun init123() {
        try {
            binding.apply {
                RcTauStat.layoutManager = LinearLayoutManager(this@StathoursActivity)
                RcTauStat.adapter = adapter

                for (i in classes.indices) {
                    val class144 = StatTauExample(
                        classes[i].toString(), subject[i].toString(),
                        hours[i]!!
                    )
                    adapter.addTauStat(class144)
                }
            }
        } catch (e: Exception) {
            Log.e("Error: ", e.message.toString())
        }

    }

    fun execute(quer: String, st: Int) {
        var statement: Statement? = null

        try {
            var myCon = MainActivity.connectionClass()?.dbConn()
            statement = myCon!!.createStatement()

            var resultSet: ResultSet =
                statement.executeQuery(quer)

            while (resultSet!!.next()) {
                classes.add(resultSet.getString("ID_Class"))
                subject.add(resultSet.getString("Name_Subject"))
                hours.add(resultSet.getInt("hours"))
            }
        } catch (e: SQLException) {
            Log.e("Error:", e.message.toString())
        }
    }
}