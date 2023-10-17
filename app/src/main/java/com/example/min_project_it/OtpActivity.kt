package com.example.min_project_it

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.min_project_it.databinding.ActivityOtpBinding


import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
//    if code sending failed we used this to resend
    private var forceResendingToken:PhoneAuthProvider.ForceResendingToken?=null
    private var mCallBacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks?=null
    private var mVerification:String?=null
    private lateinit var firebaseAuth: FirebaseAuth
    private val TAG="MAIN_TAG"

    private lateinit var progressDialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Phonell.visibility= View.VISIBLE
        binding.Codell.visibility=View.GONE
        firebaseAuth=FirebaseAuth.getInstance()

        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        mCallBacks=object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                progressDialog.dismiss()
                Log.d(TAG,"onVerificationCompleted: ")
                signInWithPhoneAuthCredential(p0)

            }

            override fun onVerificationFailed(p0: FirebaseException) {

                progressDialog.dismiss()
                Log.d(TAG,"onVerificationFailed:${p0.message}")
                Toast.makeText(this@OtpActivity,"${p0.message}",Toast.LENGTH_SHORT).show()
            }
            @SuppressLint("SetTextI18n")
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                Log.d(TAG,"onCodeSent :$p0")
                mVerification=p0
                forceResendingToken=p1
                progressDialog.dismiss()
                Log.d(TAG,"onCodeSent: $p0")
                binding.Phonell.visibility=View.GONE
                binding.Codell.visibility=View.VISIBLE
                Toast.makeText(this@OtpActivity,"Code Verification  sent",Toast.LENGTH_SHORT).show()
                binding.resendCodeTv.text="Please type the verification code we sent to ${binding.phoneEdt.text.toString().trim()}"

            }
        }
        binding.phonebtn.setOnClickListener {
            val phone=binding.phoneEdt.text.toString().trim()
            if(TextUtils.isEmpty(phone))
            {
                Toast.makeText(this@OtpActivity,"Please Enter Your Phone Number",Toast.LENGTH_SHORT).show()
            }
            else
            {
                startPhoneNumberVerification(phone)
            }


        }
        binding.resendCodeTv.setOnClickListener {
            val phone=binding.phoneEdt.text.toString().trim()
            if(TextUtils.isEmpty(phone))
            {
                Toast.makeText(this@OtpActivity,"Please Enter Your Phone Number",Toast.LENGTH_SHORT).show()
            }
            else
            {
                forceResendingToken?.let { it1 -> resendVerificationCode(phone, it1) }
            }

        }
        binding.submit.setOnClickListener {
            val code=binding.codeEdt.text.toString().trim()
            if(TextUtils.isEmpty(code))
            {
                Toast.makeText(this@OtpActivity,"Please Enter Your Verification Code",Toast.LENGTH_SHORT).show()
            }
            else
            {
                verifyPhoneNumberWithCode(mVerification, code )
            }

        }
    }
    private fun startPhoneNumberVerification(phone:String){
        Log.d(TAG,"startPhoneNumberVerification: $phone")
        progressDialog.setMessage("Verify Phone Number")
        progressDialog.show()

        val options= mCallBacks?.let {
            PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(it)
                .build()

        }
        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

    }
    private fun resendVerificationCode(phone: String,token:PhoneAuthProvider.ForceResendingToken)
    {
        progressDialog.setMessage("Resending Code")
        progressDialog.show()
        Log.d(TAG,"resendVerification:$phone")

        val options= mCallBacks?.let {
            PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(it)
                .setForceResendingToken(token)
                .build()

        }
        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

    }
    private fun verifyPhoneNumberWithCode(verificationId:String?,code:String)
    {
        Log.d(TAG,"verifyPhoneNumberWithCode:$verificationId $code")
        progressDialog.setMessage("Verifying Code..")
        progressDialog.show()
        val credential= verificationId?.let { PhoneAuthProvider.getCredential(it,code) }
        if (credential != null) {
            signInWithPhoneAuthCredential(credential)
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        Log.d(TAG,"signInWithPhoneAuthCredential:")
        progressDialog.setMessage("Logging In")
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val phone= firebaseAuth.currentUser?.phoneNumber
                Toast.makeText(this,"Logged In as $phone",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))

            }
            .addOnFailureListener {e->

                progressDialog.dismiss()
                Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
            }



    }

}