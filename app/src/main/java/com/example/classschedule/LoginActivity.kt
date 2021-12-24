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

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        findViewById<Button>(R.id.loginButton).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                connect()
            }
        })
    }

    private fun startNewActivity(email: String, password: String) {
        val intent1 = Intent(this, StartActivity::class.java)

        intent1.putExtra("loginEmail", email)
        intent1.putExtra("loginPassword", password)

        startActivity(intent1)
    }

    private fun connect() {
        var statement: Statement? = null

        try {
            var myCon = MainActivity.connectionClass()?.dbConn()
            statement = myCon!!.createStatement()

            val email = findViewById<EditText>(R.id.EmailLogin).text
            val password = findViewById<EditText>(R.id.LoginPassword).text

            var resultSet: ResultSet =
                statement.executeQuery("Select * From Admin Where Email = '$email' AND Admin_Password = '$password';")
            if (!resultSet.next()) {
                Toast.makeText(
                    this@LoginActivity,
                    "Incorrect login or password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startNewActivity(email.toString(), password.toString())
                Toast.makeText(this@LoginActivity, "Welcome", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SQLException) {
            Log.e("Error:", e.message.toString())
        }
    }
}