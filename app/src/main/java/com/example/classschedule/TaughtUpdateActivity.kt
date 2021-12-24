package com.example.classschedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import android.content.Intent
import android.widget.*


class TaughtUpdateActivity : AppCompatActivity() {

    lateinit var nameClass: String
    lateinit var nameSub1: String
    lateinit var teacherName1: String
    lateinit var hoursName1: String

    val classes: MutableList<String?> = ArrayList()
    val subject: MutableList<String?> = ArrayList()
    val teachers: MutableList<String?> = ArrayList()
    val teachersID: MutableList<String?> = ArrayList()
    var hour1: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taught_update)
        supportActionBar?.hide()

        val classSpinner1 = findViewById<Spinner>(R.id.spinnerClassSelectTauUpdate)
        val subjectSpinner1 = findViewById<Spinner>(R.id.spinnerSubjectSelectTauUpdate)
        val teacherSpinner1 = findViewById<Spinner>(R.id.spinnerTeacherSelectTauUpdate)

        nameClass = intent.getStringExtra("nameClassUpdateTaught").toString()
        nameSub1 = intent.getStringExtra("nameSubjectUpdateTaught").toString()
        teacherName1 = intent.getStringExtra("teacherNameUpdateTaught").toString()
        hoursName1 = intent.getStringExtra("hoursNameUpdateTaught").toString()

        connectSubject(
            "Select ID_Class From Class ORDER BY REPLICATE(' ',6-LEN(ID_Class))+ID_Class;",
            1
        )
        connectSubject(
            "Select Distinct s1.ID_Teacher, t1.Full_Name From Specialization s1 inner join Teacher t1 on s1.ID_Teacher = t1.ID_Teacher ORDER BY s1.ID_Teacher;",
            3
        )
        val adapter22: ArrayAdapter<String?> =
            ArrayAdapter<String?>(this, R.layout.spincustom, classes)

        val adapter23: ArrayAdapter<String?> =
            ArrayAdapter<String?>(this, R.layout.spincustom, subject)

        val adapter24: ArrayAdapter<String?> =
            ArrayAdapter<String?>(this, R.layout.spincustom, teachers)


        adapter22.setDropDownViewResource(R.layout.dropcustom)
        adapter23.setDropDownViewResource(R.layout.dropcustom)
        adapter24.setDropDownViewResource(R.layout.dropcustom)

        classSpinner1.adapter = adapter22
        subjectSpinner1.adapter = adapter23
        teacherSpinner1.adapter = adapter24

        var item1: Long = 1

        val spinnerPosition: Int = adapter22.getPosition(nameClass)
        classSpinner1.setSelection(spinnerPosition)

        val spinnerPosition1: Int = adapter23.getPosition(nameSub1)
        subjectSpinner1.setSelection(spinnerPosition1)

        val spinnerPosition12: Int = adapter24.getPosition(teacherName1)
        teacherSpinner1.setSelection(spinnerPosition12)

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (parent.getId()) {
                        R.id.spinnerTeacherSelectTauUpdate -> {
                            item1 = parent.getItemIdAtPosition(position)
                            connectSubject(
                                "Select Distinct Name_Subject From Specialization Where ID_Teacher = '${teachersID[item1.toInt()]}';",
                                2
                            )
                            hour1 = 0
                            connectSubject(
                                "Select Hours From Taught where ID_Teacher = '${teachersID[item1.toInt()]}';",
                                4
                            )
                            adapter23.notifyDataSetChanged()
                        }
                        R.id.spinnerSubjectSelectTauUpdate -> {
                            item1 = parent.getItemIdAtPosition(position)
                            connectSubject(
                                "Select Distinct ID_Teacher, (Select Full_Name from Teacher where Teacher.ID_Teacher = Taught.ID_Teacher) as Full_Name\n" +
                                        "From Taught\n" +
                                        "Where exists(Select ID_Teacher, Name_Subject from Specialization where Specialization.ID_Teacher = Taught.ID_Teacher and Specialization.Name_Subject = Taught.Name_Subject);",
                                3
                            )
                            hour1 = 0
                            connectSubject(
                                "Select Hours From Taught where ID_Teacher = '${teachersID[item1.toInt()]}';",
                                4
                            )
                            adapter23.notifyDataSetChanged()
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        subjectSpinner1.setOnItemSelectedListener(itemSelectedListener)
        classSpinner1.setOnItemSelectedListener(itemSelectedListener)
        teacherSpinner1.setOnItemSelectedListener(itemSelectedListener)

        findViewById<Button>(R.id.buttonInsertTaughtUpdate).setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(view: View?) {
                connectSubject(
                    "Update Taught set Hours = '${findViewById<EditText>(R.id.editTextHoursUpdate).text}', ID_Class = '${
                        findViewById<Spinner>(
                            R.id.spinnerClassSelectTauUpdate
                        ).selectedItem
                    }', ID_Teacher = '${teachersID[item1.toInt()]}', Name_Subject = '${
                        findViewById<Spinner>(
                            R.id.spinnerSubjectSelectTauUpdate
                        ).selectedItem
                    }' where ID_Class = '${findViewById<Spinner>(R.id.spinnerClassSelectTauUpdate).selectedItem}' and Name_Subject = '${
                        findViewById<Spinner>(
                            R.id.spinnerSubjectSelectTauUpdate
                        ).selectedItem
                    }';",
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
            val subjectResul: ResultSet

            when (num) {
                2 -> subject.clear()
                3 -> {
                    teachers.clear()
                    teachersID.clear()
                }
            }
            when (num) {
                5 -> statement.execute(nameClass1)
                else -> {
                    subjectResul = statement.executeQuery(nameClass1)
                    while (subjectResul!!.next()) {
                        when (num) {
                            1 -> classes.add(subjectResul.getString("ID_Class"))
                            2 -> subject.add(subjectResul.getString("Name_Subject"))
                            3 -> {

                                teachers.add(subjectResul.getString("Full_Name"))
                                teachersID.add(subjectResul.getString("ID_Teacher"))
                            }
                            4 -> hour1 += subjectResul.getString("Hours").toInt()
                        }
                    }
                }
            }


        } catch (e: SQLException) {
            Log.e("Error:", e.message.toString())
        }
    }
}