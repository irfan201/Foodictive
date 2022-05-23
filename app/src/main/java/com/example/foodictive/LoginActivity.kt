package com.example.foodictive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodictive.databinding.LoginActivityBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception

@Suppress("DEPRECATION")
class LoginActivity :AppCompatActivity() {
    private lateinit var binding: LoginActivityBinding
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private companion object{
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_ID_TAG"

    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

    val googleSignInOptions= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

    firebaseAuth = FirebaseAuth.getInstance()
    chckUsr()

        binding.googleSignInButton.setOnClickListener{
            Log.d(TAG, "onCreate: begin Google SignIn")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    private fun chckUsr() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            Log.d(TAG, "onActivityResult : Google SignIn intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)

            }
            catch (e:Exception){
                Log.d(TAG,"onActivityResult")
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG,"firebaseAuthWithGoogleAccount: begin firebase auth with google account")
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult->
                Log.d(TAG,"firebaseAuthWithGoogleAccount: LoggedIn")
                val firebaseUser = firebaseAuth.currentUser
                val uid = firebaseUser!!.uid
                val email = firebaseUser!!.email
                Log.d(TAG,"firebaseAuthWithGoogleAccount: Uid: $uid")
                Log.d(TAG,"firebaseAuthWithGoogleAccount: Email: $email")

                if(authResult.additionalUserInfo!!.isNewUser){
                    Log.d(TAG,"firebaseAuthWithGoogleAccount: Account created... \n$email")
                    Toast.makeText(this@LoginActivity,"Account created... \n$email", Toast.LENGTH_SHORT).show()
                }
                else{
                    Log.d(TAG,"firebaseAuthWithGoogleAccount: Existing user... \n$email")
                    Toast.makeText(this@LoginActivity,"LogedIn... \n$email", Toast.LENGTH_SHORT).show()
                }

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener{ e ->
                Log.d(TAG,"firebaseAuthWithGoogleAccount: Loggin failed due to ${e.message}")
                Toast.makeText(this@LoginActivity,"Loggin failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}