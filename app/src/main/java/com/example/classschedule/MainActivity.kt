package com.example.classschedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import java.lang.Exception
import java.sql.*
import java.util.*
import com.example.classschedule.databinding.ActivityStartBinding
import android.content.Intent

lateinit var element: String

class MainActivity : AppCompatActivity() {

    val classes: MutableList<String> = ArrayList()
    val classTeachers: MutableList<String> = ArrayList()
    lateinit var binding: ActivityStartBinding


    private val adapter = ClassList(object : Listener {
        override fun onClick(tag: String) {
            element = tag
            println(tag)
            startNewActivity()
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        execute()
        init()
    }

    private fun startNewActivity() {
        val intent1 = Intent(this, StartActivity::class.java)
        startActivity(intent1)
    }

    private fun init() {
        binding.apply {
            rcView.layoutManager = LinearLayoutManager(this@MainActivity)
            rcView.adapter = adapter

            for (i in classes.indices) {
                val class1 = ClassExample(classes[i].toString(), classTeachers[i].toString())
                adapter.addClass(class1)
            }
        }
    }

    class connectionClass {
        val ip = "192.168.31.242:1433" // your database server ip
        val db = "ClassSchedule" // your database name
        val username = "User12" // your database username
        val password = "123456" // your database password
        val url = "jdbc:jtds:sqlserver://" + ip + "/" + db

        fun dbConn(): Connection? {

            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

            var conn: Connection? = null

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                conn = DriverManager.getConnection(url, username, password)
            } catch (ex: SQLException) {
                Log.e("Error : ", ex.message.toString())
            } catch (ex1: ClassNotFoundException) {
                Log.e("Error : ", ex1.message.toString())
            } catch (ex2: Exception) {
                Log.e("Error : ", ex2.message.toString())
            }

            return conn
        }
    }

    fun execute() {
        var statement: Statement? = null

        try {
            var myCon = connectionClass()?.dbConn()
            statement = myCon!!.createStatement()

            var resultSet: ResultSet =
                statement.executeQuery("Select * From Class inner join Teacher on Class.Class_Teacher = Teacher.ID_Teacher ORDER BY REPLICATE(' ',6-LEN(ID_Class))+ID_Class;")
            while (resultSet!!.next()) {
                classes.add(resultSet.getString("ID_Class"))
                classTeachers.add(resultSet.getString("Full_Name"))
            }
        } catch (e: SQLException) {
            Log.e("Error:", e.message.toString())
        }
    }
}