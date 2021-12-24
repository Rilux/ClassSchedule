package com.example.classschedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class HeldInsert : AppCompatActivity() {
    val rooms: MutableList<String?> = ArrayList()
    val classes: MutableList<String?> = ArrayList()
    val subject: MutableList<String?> = ArrayList()
    val timeName: MutableList<String?> = ArrayList()
    val sdf = SimpleDateFormat("yyyy-MM-dd")

    lateinit var dayOfWeek1: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_held_insert)
        supportActionBar?.hide()

        val classSpinner1 = findViewById<Spinner>(R.id.spinner1)
        val subjectSpinner1 = findViewById<Spinner>(R.id.spinner2)
        val timeSpinner1 = findViewById<Spinner>(R.id.spinner3)
        val roomSpinner1 = findViewById<Spinner>(R.id.spinner4)

        dayOfWeek1 = sdf.format(findViewById<CalendarView>(R.id.calendarView1).date)

        findViewById<TextView>(R.id.dateSelected).text = dayOfWeek1

        connectSubject(
            "Select ID_Class From Class ORDER BY REPLICATE(' ',6-LEN(ID_Class))+ID_Class;",
            1
        )

        subject.add(" ")
        timeName.add(" ")
        rooms.add(" ")

        val adapter: ArrayAdapter<String?> =
            ArrayAdapter<String?>(this, android.R.layout.simple_spinner_item, classes)

        val adapter1: ArrayAdapter<String?> =
            ArrayAdapter<String?>(this, android.R.layout.simple_spinner_item, subject)

        val adapter2: ArrayAdapter<String?> =
            ArrayAdapter<String?>(this, android.R.layout.simple_spinner_item, timeName)

        val adapter3: ArrayAdapter<String?> =
            ArrayAdapter<String?>(this, android.R.layout.simple_spinner_item, rooms)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        classSpinner1.adapter = adapter
        subjectSpinner1.adapter = adapter1
        timeSpinner1.adapter = adapter2
        roomSpinner1.adapter = adapter3

        findViewById<TextView>(R.id.dateSelected).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                findViewById<CalendarView>(R.id.calendarView1).visibility = View.VISIBLE
            }
        })

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (parent.getId()) {
                        R.id.spinner1 -> {
                            val item = parent.getItemAtPosition(position) as String
                            connectSubject(
                                "Select Name_Subject From Taught Where ID_Class = '$item';",
                                2
                            )
                            adapter1.notifyDataSetChanged()
                        }
                        R.id.spinner2 -> {
                            timeName.add(" ")
                            connectSubject(
                                "Select Time_Name From Time Where NOT Exists(Select Time_Name from Held where Data = '$dayOfWeek1' and Time.Time_Name = Held.Time_Name);",
                                3
                            )
                            adapter2.notifyDataSetChanged()
                        }
                        R.id.spinner3 -> {
                            val item = parent.getItemAtPosition(position) as String
                            rooms.add(" ")
                            connectSubject(
                                "Select ID_Room From Room where Not exists (select ID_Room from held Where Data = '$dayOfWeek1' and Time_Name = '$item' and Held.ID_Room = Room.ID_Room)",
                                4
                            )
                            adapter3.notifyDataSetChanged()
                        }
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        findViewById<CalendarView>(R.id.calendarView1).setOnDateChangeListener { view, year, month, dayOfMonth ->
            dayOfWeek1 = "$year-${month + 1}-$dayOfMonth"
            findViewById<TextView>(R.id.dateSelected).text = dayOfWeek1
            findViewById<CalendarView>(R.id.calendarView1).visibility = View.INVISIBLE
            timeName.add(" ")
            connectSubject(
                "Select Time_Name From Time Where NOT Exists(Select Time_Name from Held where Data = '$dayOfWeek1' and Time.Time_Name = Held.Time_Name);",
                3
            )
        }

        subjectSpinner1.setOnItemSelectedListener(itemSelectedListener)
        classSpinner1.setOnItemSelectedListener(itemSelectedListener)
        timeSpinner1.setOnItemSelectedListener(itemSelectedListener)

        findViewById<Button>(R.id.button1).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                connectSubject(
                    "Insert into Held values('${findViewById<Spinner>(R.id.spinner4).selectedItem}', '${
                        findViewById<Spinner>(
                            R.id.spinner3
                        ).selectedItem
                    }', '${dayOfWeek1.toString()}', '${findViewById<Spinner>(R.id.spinner2).selectedItem}', '${
                        findViewById<Spinner>(
                            R.id.spinner1
                        ).selectedItem
                    }')", 5
                )
            }
        })
    }


    fun connectSubject(nameClass1: String, num: Int) {
        var statement: Statement? = null

        try {
            val myCon = MainActivity.connectionClass()?.dbConn()
            statement = myCon!!.createStatement()
            val subjectResul: ResultSet = statement.executeQuery(nameClass1)

            when (num) {
                5 -> statement.execute(nameClass1)
            }

            when (num) {
                2 -> subject.clear()
                3 -> timeName.clear()
                4 -> rooms.clear()
            }

            while (subjectResul!!.next()) {
                when (num) {
                    1 -> classes.add(subjectResul.getString("ID_Class"))
                    2 -> subject.add(subjectResul.getString("Name_Subject"))
                    3 -> timeName.add(subjectResul.getString("Time_Name"))
                    4 -> rooms.add(subjectResul.getString("ID_Room"))
                }
            }

        } catch (e: SQLException) {
            Log.e("Error:", e.message.toString())
        }
    }
}