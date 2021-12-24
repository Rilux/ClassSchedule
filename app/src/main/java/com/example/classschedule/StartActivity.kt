package com.example.classschedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.classschedule.databinding.ActivityMainBinding
import java.lang.Exception
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.*
import android.content.Intent
import android.view.View
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlin.collections.ArrayList
import noman.weekcalendar.WeekCalendar
import noman.weekcalendar.listener.OnDateClickListener
import java.text.SimpleDateFormat


class StartActivity : AppCompatActivity() {
    val rooms: MutableList<String> = ArrayList()
    val classes: MutableList<String> = ArrayList()
    val subject: MutableList<String> = ArrayList()
    val timeName: MutableList<String> = ArrayList()
    val dateSche: MutableList<Date> = ArrayList()

    val sdf = SimpleDateFormat("yyyy-MM-dd")

    var dayOfWeek1: String = sdf.format(Date())
    lateinit var name: String
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        name = element

        val ss: String? = intent.getStringExtra("loginEmail").toString()

        if (ss != "null") {
            var menuNav = findViewById<NavigationView>(R.id.navigation_view1).getMenu()
            menuNav.findItem(R.id.adminFunctions).isVisible = true
        }

        binding.apply {

            toolbarTitle.text = name

            navigationView1.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.login -> startNewActivityLogin()
                    R.id.register -> startNewActivityRegister()
                    R.id.addNewLessonHeld -> startNewActivityAddLesson()
                    R.id.addNewClassRoom -> startNewActivityClass()
                    R.id.addNewSpec -> startNewActivitySpec()
                    R.id.addNewTaught -> startNewActivityTaught()
                    R.id.editSpec -> startNewActivityEditSpec()
                    R.id.editTaught -> startNewActivityTaughtEdit()
                    R.id.statistics -> startNewActivityStatistics()
                    R.id.statTau -> startNewActivityTaughtStat()
                }
                true
            }
            try {
                for (i in 1..8) {
                    val resId: Int = binding.root.getResources()
                        .getIdentifier("textLesson$i", "id", this@StartActivity.getPackageName())
                    findViewById<TextView>(resId).setOnClickListener(object : View.OnClickListener {
                        override fun onClick(view: View?) {
                            if (findViewById<TextView>(resId).text != "$i Lesson") {
                                val nameLes = timeName[i - 1]
                                connect2(nameLes)
                                slidingLay.visibility = View.VISIBLE
                            } else {
                                findViewById<TextView>(R.id.TeacherName).text =
                                    "Teacher: "
                                findViewById<TextView>(R.id.LessoonName).text =
                                    "Subject: "
                                findViewById<TextView>(R.id.RoomName).text =
                                    "Room: "
                                findViewById<TextView>(R.id.TimeName).text =
                                    "Time: "
                            }
                        }
                    })
                }
            } catch (e: Exception) {
                Log.e("Error: ", e.message.toString())
            }

            menuButton.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    drawer.openDrawer(GravityCompat.START)
                }
            })
        }

        try {
            findViewById<WeekCalendar>(R.id.weekCalendar).setOnDateClickListener(OnDateClickListener { date ->
                dayOfWeek1 = date.toString()
                timeName.clear()
                rooms.clear()
                subject.clear()
                dateSche.clear()
                connect1()
            })
        } catch (e: Exception) {
            Log.e("Error:", e.message.toString())
        }

        connect1()
    }

    fun connect1() {
        var statement: Statement? = null

        try {
            val myCon = MainActivity.connectionClass()?.dbConn()
            statement = myCon!!.createStatement()

            val resultSet: ResultSet =
                statement.executeQuery("Select * From Held Where ID_Class = '$name' AND Data = '$dayOfWeek1';")

            while (resultSet!!.next()) {
                rooms.add(resultSet.getString("ID_Room"))
                classes.add(resultSet.getString("ID_Class"))
                subject.add(resultSet.getString("Name_Subject"))
                timeName.add(resultSet.getString("Time_Name"))
                dateSche.add(resultSet.getDate("Data"))
            }
            AddLesson()
        } catch (e: SQLException) {
            Log.e("Error:", e.message.toString())
        }
    }

    fun connect2(nameLes: String) {
        var statement: Statement? = null

        try {
            val myCon = MainActivity.connectionClass()?.dbConn()
            statement = myCon!!.createStatement()

            val resultSet: ResultSet =
                statement.executeQuery(
                    "Select *, FORMAT(Start_Time, N'hh\\.mm') as Start_Time1, FORMAT(End_Time, N'hh\\.mm') as End_Time1, (Select Full_Name from Teacher where Teacher.ID_Teacher = Taught.ID_Teacher) as Teacher From Held INNER JOIN Time on Held.Time_Name = Time.Time_Name " +
                            "INNER JOIN Taught on Taught.ID_Class = Held.ID_Class AND Taught.Name_Subject = Held.Name_Subject  Where Held.Time_Name = '$nameLes' AND Data = '$dayOfWeek1';"
                )

            while (resultSet!!.next()) {
                findViewById<TextView>(R.id.TeacherName).text =
                    "Teacher: " + resultSet.getString("Teacher")
                findViewById<TextView>(R.id.LessoonName).text =
                    "Subject: " + resultSet.getString("Name_Subject")
                findViewById<TextView>(R.id.RoomName).text =
                    "Room: " + resultSet.getString("ID_Room") + " room"
                findViewById<TextView>(R.id.TimeName).text =
                    "Time: " + resultSet.getString("Start_Time1") + " - " + resultSet.getString("End_Time1")
            }
        } catch (e: SQLException) {
            Log.e("Error:", e.message.toString())
        }
    }

    fun AddLesson() {
        try {
            val any = if (this == null) "1" else "2"

            setLesson()
            for (i in 0..7) {
                val textLessons =
                    timeName[i] + " " + subject[i]

                when (timeName[i]) {
                    "1 Lesson" -> findViewById<TextView>(R.id.textLesson1).text = textLessons
                    "2 Lesson" -> findViewById<TextView>(R.id.textLesson2).text = textLessons
                    "3 Lesson" -> findViewById<TextView>(R.id.textLesson3).text = textLessons
                    "4 Lesson" -> findViewById<TextView>(R.id.textLesson4).text = textLessons
                    "5 Lesson" -> findViewById<TextView>(R.id.textLesson5).text = textLessons
                    "6 Lesson" -> findViewById<TextView>(R.id.textLesson6).text = textLessons
                    "7 Lesson" -> findViewById<TextView>(R.id.textLesson7).text = textLessons
                    "8 Lesson" -> findViewById<TextView>(R.id.textLesson8).text = textLessons
                }
            }
        } catch (e: Exception) {
            Log.e("Error: ", e.message.toString())
        }
    }

    private fun setLesson() {
        for (i in 1..8) {
            val resId: Int = binding.root.getResources()
                .getIdentifier("textLesson$i", "id", this@StartActivity.getPackageName())
            findViewById<TextView>(resId).text = "$i Lesson"
        }
    }

    private fun startNewActivityLogin() {
        val intent1 = Intent(this, LoginActivity::class.java)
        startActivity(intent1)
    }

    private fun startNewActivityRegister() {
        val intent1 = Intent(this, RegisterActivity::class.java)
        startActivity(intent1)
    }

    private fun startNewActivityAddLesson() {
        val intent1 = Intent(this, HeldInsert::class.java)
        startActivity(intent1)
    }

    private fun startNewActivityClass() {
        val intent1 = Intent(this, ClassRoomSubjectInsertActivity::class.java)
        startActivity(intent1)
    }

    private fun startNewActivitySpec() {
        val intent1 = Intent(this, SpicializInsertActivity::class.java)
        startActivity(intent1)
    }

    private fun startNewActivityTaught() {
        val intent1 = Intent(this, TaughtInsertActivity::class.java)
        startActivity(intent1)
    }

    private fun startNewActivityEditSpec() {
        val intent1 = Intent(this, SpecUpdateDeleteActivity::class.java)
        startActivity(intent1)
    }

    private fun startNewActivityTaughtEdit() {
        val intent1 = Intent(this, TaughtUpdateInsert::class.java)
        startActivity(intent1)
    }

    private fun startNewActivityStatistics() {
        val intent1 = Intent(this, StatisticsActivity::class.java)
        startActivity(intent1)
    }

    private fun startNewActivityTaughtStat() {
        val intent1 = Intent(this, StathoursActivity::class.java)
        startActivity(intent1)
    }
}