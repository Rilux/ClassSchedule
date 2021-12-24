 package com.example.classschedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

 class StatisticsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        supportActionBar?.hide()
        var st: String = "select distinct top 1 (Select Full_Name from teacher where taught.ID_Teacher = Teacher.ID_Teacher) as Full_Name, count(*) over(partition by ID_Teacher) as cmax\n" +
        "From held \n" +
                "\tinner join taught on held.ID_Class = Taught.ID_Class and held.Name_Subject = Taught.Name_Subject\n" +
                "order by cmax desc"
        connect1(st, 1)
        st = "select distinct top 1 ID_Room, count(*) over(partition by ID_Room) as cmax\n" +
                "From held \n" +
                "order by cmax desc"
        connect1(st, 2)
        st = "select distinct top 1 ID_Class, count(*) over(partition by ID_Class) as cmax\n" +
                "From held \n" +
                "order by cmax desc"
        connect1(st, 3)
        st = "select distinct top 1 Time_Name, count(*) over(partition by Time_Name) as cmax\n" +
                "From held \n" +
                "order by cmax desc"
        connect1(st, 4)
    }

    fun connect1(st: String, ca1: Int) {
        var statement: Statement? = null

        try {
            val myCon = MainActivity.connectionClass()?.dbConn()
            statement = myCon!!.createStatement()

            val resultSet: ResultSet =
                statement.executeQuery(st)

            while (resultSet!!.next()) {
                when(ca1){
                    1 -> findViewById<TextView>(R.id.textBisiestTeacher).text = resultSet.getString("Full_name") + " " + resultSet.getString("cmax") + " lessons"
                    2 -> findViewById<TextView>(R.id.textView3).text = "№ " + resultSet.getString("ID_Room") + " - " + resultSet.getString("cmax") + " lessons"
                    3 -> findViewById<TextView>(R.id.textView7).text = resultSet.getString("ID_Class") + " - " + resultSet.getString("cmax") + " lessons"
                    4 -> findViewById<TextView>(R.id.textView10).text = resultSet.getString("Time_Name") + " - " + resultSet.getString("cmax") + " lessons"
                }
            }
        } catch (e: SQLException) {
            Log.e("Error:", e.message.toString())
        }
    }
}