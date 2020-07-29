package com.example.login

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User
import javax.annotation.meta.Exclusive


class sign_up : AppCompatActivity() {

     lateinit var fullName:EditText
     lateinit var passwordUser:EditText
     lateinit var emailUser:EditText
     lateinit var mobUser:EditText
     lateinit var mSignUpButton: Button
     lateinit var loadingDialog:ProgressDialog
     lateinit var RootRef: DatabaseReference
     lateinit var postReference:DatabaseReference
    fun SignUp(view:View)
    {
        createAccount()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        fullName=findViewById<EditText>(R.id.editTextNameSignup)
        passwordUser=findViewById<EditText>(R.id.editTextPasswordSignup)
        emailUser=findViewById<EditText>(R.id.editTextTextEmailSignup)
        mobUser=findViewById<EditText>(R.id.editTextPhone)
        mSignUpButton=findViewById<Button>(R.id.buttonSignup)
        loadingDialog=ProgressDialog(this)
    }
    fun validateEmail(name: String, email: String, getpassword: String, mob: String) {
        RootRef=FirebaseDatabase.getInstance().getReference("users")
        //RootRef.addListenerForSingleValueEvent(object:ValueEventListener {
           // override fun onDataChange(dataSnapshot: DataSnapshot) {
                //Log.i("I am here","I am here")
               // if (!(dataSnapshot.child("users").child(email).exists())) {
                    val key=RootRef.child("users").push().key
                    if (key==null){
                        Toast.makeText(this@sign_up,"Key Not Created",Toast.LENGTH_SHORT).show()
                        loadingDialog.dismiss()
                        return
                    }
                    val users= User(name,email,getpassword,mob)
                    val userValues=users.toMap()
                    var userDataMap= hashMapOf<String,Any>("/users/&key" to userValues,"/email/$name/$getpassword/$mob/&key" to userValues)
                   /* userDataMap.put("/user/$key",userValues)
                    userDataMap.put("/Email/$key",email)
                    userDataMap.put("/Name/",name)
                    userDataMap.put("password",getpassword)
                    userDataMap.put("phone",mob)*/
                    //RootRef.child("Users").child(email).updateChildren(userDataMap)
                    RootRef.updateChildren(userDataMap)
                    .addOnSuccessListener{
                                Toast.makeText(this@sign_up,"Account Created",Toast.LENGTH_SHORT).show()
                                loadingDialog.dismiss()
                        }
                        .addOnFailureListener{
                            Toast.makeText(this@sign_up,"Account not created",Toast.LENGTH_SHORT).show()
                            loadingDialog.dismiss()
                        }
               /* } else {
                    val context: Context = this@sign_up
                    Toast.makeText(context, "Email exists", Toast.LENGTH_SHORT).show()
                    loadingDialog.dismiss()
                }
            }*/
            // override fun onCancelled(p0: DatabaseError) {
               // Log.w("Error","loadPost:onCancelled",p0.toException())
              //  Toast.makeText(this@sign_up,"Account not created",Toast.LENGTH_SHORT).show()
           // }
        //}

    }
    fun createAccount() {
        val name=fullName.text.toString()
        val getpassword=passwordUser.text.toString()
        val getEmail=emailUser.text.toString()
        val getMob=mobUser.text.toString()
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Enter Name!",Toast.LENGTH_SHORT).show()
        }
        else if(TextUtils.isEmpty(getpassword)){
            Toast.makeText(this,"Enter Password!",Toast.LENGTH_SHORT).show()
        }
        else if(TextUtils.isEmpty(getEmail)){
            Toast.makeText(this,"Enter Email!",Toast.LENGTH_SHORT).show()
        }
        else if(TextUtils.isEmpty(getMob)){
            Toast.makeText(this,"Enter Mobile!",Toast.LENGTH_SHORT).show()
        }
        else{
            loadingDialog.setTitle("Creating Account")
            loadingDialog.setMessage("Please Wait")
            loadingDialog.setCanceledOnTouchOutside(false)
            loadingDialog.show()
            validateEmail(name,getEmail,getpassword,getMob)
        }
    }
    @IgnoreExtraProperties
    data class User(
    var UserName:String?=" ",
    var UserEmail:String?=" ",
    var UserPssword:String?="",
    var UserMob:String?="")
    {
        @Exclude
        fun toMap(): Map<String,Any?>{
            return mapOf(
                "Name" to UserName,
                "Email" to UserEmail,
                "Password" to UserPssword,
                "Mobile" to UserMob
            )
        }
    }
}