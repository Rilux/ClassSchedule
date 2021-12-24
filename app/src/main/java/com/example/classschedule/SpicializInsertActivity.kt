package com.example.classschedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class SpicializInsertActivity : AppCompatActivity() {

    val subject: MutableList<String?> = ArrayList()
    val teachers: MutableList<String?> = ArrayList()
    val teachersID: MutableList<String?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spicializ_insert)
        supportActionBar?.hide()

        connectSubject(
            "Select * From Teacher order by ID_Teacher;",
            1
        )
        connectSubject(
            "Select * From Subject;",
            2
        )
        val teacherSpinner1 = findViewById<Spinner>(R.id.spinnerTeacherSelect)
        val subjectSpinner1 = findViewById<Spinner>(R.id.spinnerSubjectSelect)

        val adapter11: ArrayAdapter<String?> =
            ArrayAdapter<String?>(this, R.layout.spincustom, teachers)
        val adapter12: ArrayAdapter<String?> =
            ArrayAdapter<String?>(this, R.layout.spincustom, subject)

        adapter11.setDropDownViewResource(R.layout.dropcustom)
        teacherSpinner1.adapter = adapter11

        adapter12.setDropDownViewResource(R.layout.dropcustom)
        subjectSpinner1.adapter = adapter12

        var item1: Long = 1

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (parent.getId()) {
                        R.id.spinnerTeacherSelect -> {
                            item1 = parent.getItemIdAtPosition(position)
                            connectSubject(
                                "Select * From Teacher order by ID_Teacher;",
                                1
                            )
                            adapter11.notifyDataSetChanged()
                        }
                        R.id.spinnerSubjectSelect -> {
                            connectSubject(
                                "Select * From Subject;",
                                2
                            )
                            adapter12.notifyDataSetChanged()
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        subjectSpinner1.setOnItemSelectedListener(itemSelectedListener)
        teacherSpinner1.setOnItemSelectedListener(itemSelectedListener)

        findViewById<Button>(R.id.buttonSpicInsert).setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(view: View?) {
                connectSubject(
                    "Insert into Specialization values('${teachersID[item1.toInt()]}', '${
                        findViewById<Spinner>(
                            R.id.spinnerSubjectSelect
                        ).selectedItem
                    }')",
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
                    2 -> {
                        subject.add(subjectResul.getString("Name_Subject"))
                    }
                }
            }

        } catch (e: SQLException) {
            Log.e("Error:", e.message.toString())
        }
    }
}