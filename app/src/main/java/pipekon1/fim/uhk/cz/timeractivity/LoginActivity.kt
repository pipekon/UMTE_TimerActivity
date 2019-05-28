package pipekon1.fim.uhk.cz.timeractivity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)





        signin_button.setOnClickListener {
            login()
        }

        register_button.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }

    private fun login() {
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this, "Successfully logged in", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error, bad username or password", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            Toast.makeText(this, "Please fill up the credentials", Toast.LENGTH_LONG).show()
        }
    }


}