package com.example.classschedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import java.lang.Exception
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.text.SimpleDateFormat

val rooms: MutableList<String?> = ArrayList()
val classes: MutableList<String?> = ArrayList()
val subject: MutableList<String?> = ArrayList()
val teachers: MutableList<String?> = ArrayList()
val teachersID: MutableList<String?> = ArrayList()

class ClassRoomSubjectInsertActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_room_subject_insert)
        supportActionBar?.hide()


        connectSubject(
            "Select * From Teacher order by ID_Teacher;",
            1
        )

        val teacherSpinner1 = findViewById<Spinner>(R.id.spinnerInsertClass)

        val adapter13: ArrayAdapter<String?> =
            ArrayAdapter<String?>(this, R.layout.spincustom, teachers)

        adapter13.setDropDownViewResource(R.layout.dropcustom)
        teacherSpinner1.adapter = adapter13

        var item1: Long = 1

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    item1 = parent.getItemIdAtPosition(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        teacherSpinner1.setOnItemSelectedListener(itemSelectedListener)

        findViewById<Button>(R.id.buttonInsertClass).setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(view: View?) {
                connectSubject(
                    "Insert into Class values('${findViewById<EditText>(R.id.editTextTextPersonName).text}', '${teachersID[item1.toInt()]}')",
                    5
                )
            }
        })

        findViewById<Button>(R.id.buttonInsertRoom).setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(view: View?) {
                connectSubject(
                    "Insert into Room values('${findViewById<EditText>(R.id.insertRoomNumber).text}', '${
                        findViewById<EditText>(
                            R.id.insertFloorNumber
                        ).text
                    }')", 5
                )
            }
        })

        findViewById<Button>(R.id.buttonInsertSubject).setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(view: View?) {
                connectSubject(
                    "Insert into Subject values('${findViewById<EditText>(R.id.insertSubject).text}')",
                    5
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
                1 -> {
                    teachers.clear()
                    teachersID.clear()
                }
            }

            while (subjectResul!!.next()) {
                when (num) {
                    1 -> {
                        teachers.add(subjectResul.getString("Full_Name"))
                        teachersID.add(subjectResul.getString("ID_Teacher"))
                    }
                }
            }

        } catch (e: SQLException) {
            Log.e("Error:", e.message.toString())
        }
    }
}