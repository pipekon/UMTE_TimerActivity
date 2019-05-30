package pipekon1.fim.uhk.cz.timeractivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import pipekon1.fim.uhk.cz.timeractivity.util.TimerExpiredReceiveer

import java.util.*

class RegisterActivity : AppCompatActivity() {

    private val MY_REQUEST_CODE: Int = 7117


    lateinit var providers : List<AuthUI.IdpConfig>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //init
        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )

        showSignInOptions()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    if(requestCode == MY_REQUEST_CODE){
        val response =  IdpResponse.fromResultIntent(data)
        if(resultCode == Activity.RESULT_OK)
        {
            val user = FirebaseAuth.getInstance().currentUser //aktual uzivatel
            Toast.makeText(this, ""+user!!.email,Toast.LENGTH_SHORT).show()

         /*   //ulozeni
            val uid = FirebaseAuth.getInstance().uid ?: ""
            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
            val email = user!!.email

            if(email != null) {
                val user = User(uid, email)
            }

            ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Finally we saved the user to Firebase Database")
                }
                .addOnFailureListener {
                    Log.d("RegisterActivity", "Failed to set value to database: ${it.message}")
                }*/

            // btn_sign_out.isEnabled = true
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }
        else
        {
            Toast.makeText(this, ""+response!!.error!!.message,Toast.LENGTH_SHORT).show()
        }
    }
    }

    private fun showSignInOptions() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.MyTheme)
            .build(),MY_REQUEST_CODE)
    }

}

/*
class User(val uid: String, val email: String)*/
