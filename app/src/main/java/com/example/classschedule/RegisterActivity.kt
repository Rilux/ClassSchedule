package com.example.classschedule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        findViewById<Button>(R.id.registerbutton).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                connect()
            }
        })
    }

    private fun connect() {
        var statement: Statement? = null
        var statement2: Statement? = null
        var statement3: Statement? = null
        var statement4: Statement? = null
        try {
            var myCon = MainActivity.connectionClass()?.dbConn()

            statement = myCon!!.createStatement()
            statement2 = myCon!!.createStatement()
            statement3 = myCon!!.createStatement()
            statement4 = myCon!!.createStatement()

            val email = findViewById<EditText>(R.id.RegisterEmail).text
            val password = findViewById<EditText>(R.id.RegisterPassword).text
            val schoolPassword = findViewById<EditText>(R.id.RegisterSchoolPassword).text
            val fullname = findViewById<EditText>(R.id.FullName).text

            var checkEmail: ResultSet =
                statement.executeQuery("Select * From Admin Where Email = '$email';")

            var checkSchoolPassword: ResultSet =
                statement2.executeQuery("Select * From SchoolPassword Where Password = '$schoolPassword';")

            if (checkEmail.next()) {
                Toast.makeText(
                    this@RegisterActivity,
                    "This Email already exists",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!checkSchoolPassword.next()) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Incorrect school password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                statement3.execute("Insert into Admin(Email, Admin_Password, Password) Values ('$email','$password', '$schoolPassword');")
                statement4.execute("Insert into Teacher(Full_Name, Hours_Per_Year, Email) values('$fullname', NULL, '$email')")

                startNewActivity(email.toString(), password.toString())

                Toast.makeText(this@RegisterActivity, "Welcome", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SQLException) {
            Log.e("Error:", e.message.toString())
        }
    }

    private fun startNewActivity(email: String, password: String) {
        val intent1 = Intent(this, StartActivity::class.java)

        intent1.putExtra("loginEmail", email)
        intent1.putExtra("loginPassword", password)

        startActivity(intent1)
    }
}