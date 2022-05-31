package com.mistershorr.birthdaytracker

import android.R.attr
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.backendless.Backendless
import com.mistershorr.birthdaytracker.databinding.ActivityLoginBinding
import com.backendless.exceptions.BackendlessFault

import com.backendless.BackendlessUser

import com.backendless.async.callback.AsyncCallback

import android.R.attr.password
import android.util.Log
import android.widget.Toast


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // any time you need what would have been a "static" variable in java, you use
    // companion object in Kotlin. You access with ClassName.VARIABLE_NAME like Math.PI
    companion object {
        // keys for the key-value pairs for the intent extras
        val EXTRA_USERNAME = "username"
        val EXTRA_PASSWORD = "password"
        val EXTRA_USER_ID = "userid"
    }

    // starting an activity for a result
    // Step 1: Register the Activity with a contract
    val startRegistrationForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        // decide what to do if the result is ok (if it was successful)
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            // Handle the Intent
            // note: editTexts are different from textViews in that you have to call setText
            binding.editTextLoginUsername.setText(intent?.getStringExtra(EXTRA_USERNAME))
            binding.editTextLoginPassword.setText(intent?.getStringExtra(EXTRA_PASSWORD))
        }
        // decide what to do if it was unsuccessful with a RESULT_CANCELED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Backendless.initApp( this, Constants.APP_ID, Constants.API_KEY )

        binding.buttonLoginLogin.setOnClickListener {
            // do not forget to call Backendless.initApp in the app initialization code

            val username = binding.editTextLoginUsername.text.toString()
            val password = binding.editTextLoginPassword.text.toString()
            Backendless.UserService.login(
                username,
                password,
                object : AsyncCallback<BackendlessUser?> {
                    override fun handleResponse(user: BackendlessUser?) {
                        // user has been logged in
                        Log.d("LoginActivity", "handleResponse: ${user?.email}")
                        Toast.makeText(this@LoginActivity,
                            "${user?.getProperty("username")} has logged in successfully",
                            Toast.LENGTH_SHORT).show()

                        val birthdayListIntent = Intent(this@LoginActivity, BirthdayListActivity:: class.java)
                        birthdayListIntent.putExtra(EXTRA_USER_ID, user?.objectId)
                        startActivity(birthdayListIntent)
                        finish()
                    }

                    override fun handleFault(fault: BackendlessFault) {
                        // login failed, to get the error code call fault.getCode()
                        Log.d("LoginActivity", "handleFault: ${fault.message}")
                    }
                })

        }

        binding.textViewLoginCreateAccount.setOnClickListener {
            // launch the registration activity
            // pass the values of username and password along to the new activity
            // 1. extract any information you might need from edit texts
            val username = binding.editTextLoginUsername.text.toString()
            val password = binding.editTextLoginPassword.text.toString()

            // 2. create the intent and package the extras
            // the context is the activity you are in (here we can use this)
            val registrationIntent
            = Intent(this, RegistrationActivity::class.java).apply {
                putExtra(EXTRA_USERNAME, username)
                putExtra(EXTRA_PASSWORD, password)
            }

            // 3. launch the activity
//            startActivity(registrationIntent)
            // 3b. Alternate: Could launch the activity for a result instead
            // use the variable from the register for result contract above
            startRegistrationForResult.launch(registrationIntent)

        }
    }
}