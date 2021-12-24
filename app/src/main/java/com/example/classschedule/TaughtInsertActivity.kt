package com.example.classschedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class TaughtInsertActivity : AppCompatActivity() {

    val classes: MutableList<String?> = ArrayList()
    val subject: MutableList<String?> = ArrayList()
    val teachers: MutableList<String?> = ArrayList()
    val teachersID: MutableList<String?> = ArrayList()
    var hour1: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taught_insert)
        supportActionBar?.hide()

        val classSpinner1 = findViewById<Spinner>(R.id.spinnerClassSelectTau)
        val subjectSpinner1 = findViewById<Spinner>(R.id.spinnerSubjectSelectTau)
        val teacherSpinner1 = findViewById<Spinner>(R.id.spinnerTeacherSelectTau)

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

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (parent.getId()) {
                        R.id.spinnerTeacherSelectTau -> {
                            item1 = parent.getItemIdAtPosition(position)
                            connectSubject(
                                "Select Name_Subject From Specialization Where ID_Teacher = '${teachersID[item1.toInt()]}';",
                                2
                            )
                            hour1 = 0
                            connectSubject("Select Hours From Taught where ID_Teacher = '${teachersID[item1.toInt()]}';", 4)
                            adapter23.notifyDataSetChanged()
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        subjectSpinner1.setOnItemSelectedListener(itemSelectedListener)
        classSpinner1.setOnItemSelectedListener(itemSelectedListener)
        teacherSpinner1.setOnItemSelectedListener(itemSelectedListener)



        findViewById<Button>(R.id.buttonInsertTaught).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val hr = hour1 + findViewById<EditText>(R.id.editTextHours).text.toString().toInt()
                if (hr <= 240) {
                    connectSubject("Insert into Taught values('${findViewById<EditText>(R.id.editTextHours).text}', '${findViewById<Spinner>(R.id.spinnerClassSelectTau).selectedItem}', '${teachersID[item1.toInt()]}', '${findViewById<Spinner>(R.id.spinnerSubjectSelectTau).selectedItem}')", 5)
                }else{
                    Toast.makeText(
                        this@TaughtInsertActivity,
                        "The teacher has too many hours",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
            }

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

        } catch (e: SQLException) {
            Log.e("Error:", e.message.toString())
        }
    }
}