package pipekon1.fim.uhk.cz.timeractivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openTimer.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }


        btn_sign_out.setOnClickListener {
            AuthUI.getInstance().signOut(this)
                .addOnCompleteListener {
                    btn_sign_out.isEnabled = false

                }
                .addOnFailureListener {
                        e->
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
        }



        }
    }



