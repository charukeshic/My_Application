package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myapplication.ContactInformation.Mailer.sendMail
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class ContactInformation : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var menuIcon: ImageView

    lateinit var saveChanges : Button
    lateinit var userImage : ImageView
    lateinit var selectPhoto : Button
    lateinit var sendButton : Button
    var btnState : Int = 0

    lateinit var description : EditText
    lateinit var chatbot: FloatingActionButton

    private lateinit var ImageUri : Uri

    lateinit var layoutHeader : View
    lateinit var userName : TextView
    lateinit var userEmail : TextView
    lateinit var existingImage : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_contact_information)

        /*------------Hooks--------------*/
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        menuIcon = findViewById(R.id.menu_icon)
        chatbot = findViewById(R.id.chatbot)

        navigationView.setCheckedItem(R.id.nav_contact)

        saveChanges = findViewById(R.id.signUpButton)
        selectPhoto = findViewById(R.id.select_image)
        userImage = findViewById(R.id.user_image2)
        sendButton = findViewById(R.id.call_btn)

        description = findViewById(R.id.email1)

        selectPhoto.setOnClickListener {

            btnState = 1
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        saveChanges.setOnClickListener {

            val uid = UUID.randomUUID().toString().substring(0,6)
            val user = FirebaseAuth.getInstance().currentUser?.email.toString()

            val image = "No image provided"
            val currentDateTime = LocalDateTime.now()
            val feedbackDate = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val newFeedback = Feedback(uid, image, description.text.toString(), feedbackDate.toString(), user)

            update(newFeedback)

           //sendMail("$uid", newFeedback)

            description.text.clear()
            userImage.setImageURI(null)
            btnState = 0


        }

        sendButton.setOnClickListener {

            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode("01131074570")))
            startActivity(intent)

        }

        updateNavHeader()

        navigationDrawer()

        chatbot.setOnClickListener {

            val chat = "https://dialogflow.cloud.google.com/#/agent/shoppingchatbot-alpv/integrations"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(("$chat")))
            startActivity(intent)


        }




    }


    object Config {
        const val EMAIL_FROM = "onlineaishoppingassistant@gmail.com"
        const val PASS_FROM = "PERFECTFYP"
    }


    object Mailer {

        init {
            Security.addProvider(BouncyCastleProvider())
        }

        private fun props(): Properties = Properties().also {
            // Smtp server
            it["mail.smtp.host"] = "smtp.gmail.com"
            // Change when necessary
            it["mail.smtp.auth"] = "true"
            it["mail.smtp.port"] = "465"
            // Easy and fast way to enable ssl in JavaMail
            it["mail.smtp.ssl.enable"] = true
        }

        // Dont ever use "getDefaultInstance" like other examples do!
        private fun session(emailFrom: String, emailPass: String): Session =
            Session.getInstance(props(), object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(emailFrom, emailPass)
                }
            })

        private fun builtMessage(firstName: String, order: Feedback): String {
            return """
            <b>Here is what we received from you</b> <br/>
            <br/>
            <b>Feedback ID:</b> $firstName  <br/>
            <b>Issue Date:</b> ${order.feedbackDate} <br/>
            <b>Feedback:</b> ${order.feedback} <br/>
            <br/>
            If you have any enquiries or did not give this feedback please contact us at 011-31074570 <br/>
            
        """.trimIndent()
        }

        private fun builtSubject(firstName: String, order: Feedback): String {
            return """
            Your feedback has been sent successfully
        """.trimIndent()
        }

        private fun sendMessageTo(
            emailFrom: String,
            session: Session,
            message: String,
            subject: String
        ) {
            try {
                MimeMessage(session).let { mime ->
                    mime.setFrom(InternetAddress(emailFrom))
                    // Adding receiver
                    val uid = FirebaseAuth.getInstance().currentUser?.email.toString()
                    mime.addRecipient(Message.RecipientType.TO, InternetAddress(uid))
                    // Adding subject
                    mime.subject = subject
                    // Adding message
                    mime.setText(message)
                    // Set Content of Message to Html if needed
                    mime.setContent(message, "text/html")
                    // send mail
                    Transport.send(mime)
                }

            } catch (e: MessagingException) {
                Log.e("", "") // Or use timber, it really doesn't matter
            }
        }

        fun sendMail(firstName: String, order: Feedback) {
            // Open a session
            val session = session(Config.EMAIL_FROM, Config.PASS_FROM)

            // Create a message
            val message = builtMessage(firstName, order)

            // Create subject
            val subject = builtSubject(firstName, order)

            // Send Email
            CoroutineScope(Dispatchers.IO).launch {
                sendMessageTo(
                    Config.EMAIL_FROM,
                    session,
                    message,
                    subject
                )
            }
        }

    }

    private fun updateNavHeader() {

        layoutHeader = navigationView.getHeaderView(0)
        userName = layoutHeader.findViewById(R.id.username1)
        existingImage = layoutHeader.findViewById(R.id.user_image)
        userEmail = layoutHeader.findViewById(R.id.email1)


        val uid = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("Profile Activity", "username: $uid")
        val ref = FirebaseDatabase.getInstance().getReference("/Users").child("$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                userName.text = user?.username.toString()
                userEmail.text = user?.email.toString()
                Picasso.get().load(user?.image).into(existingImage)

            }

        })


    }



    private fun update(feedback : Feedback) {

        if (description.text.isNotEmpty()) {

            saveFeedback(feedback)
            Toast.makeText(this@ContactInformation, "Your feedback has been received, our team will contact you.", Toast.LENGTH_LONG).show()
        }

        else
            Toast.makeText(this@ContactInformation, "No feedback was given", Toast.LENGTH_SHORT).show()

    }

    private fun saveFeedback(feedback : Feedback) {

        val uid = feedback.feedbackId
        val ref = FirebaseDatabase.getInstance().getReference("/Feedback").child(uid)

        if(btnState == 1) {
            saveImage(feedback)
            ref.setValue(feedback)
            sendMail("$uid", feedback)
        }
        else if (btnState == 0) {
            ref.setValue(feedback)
            sendMail("$uid", feedback)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            userImage.setImageURI(data?.data)

            ImageUri = data?.data!!

        }

    }

    private fun saveImage(feedback: Feedback) {

        val uid = UUID.randomUUID().toString().substring(0,4)
        val fileName = uid.plus("(").plus(FirebaseAuth.getInstance().currentUser?.email.toString()).plus(")")

        val feedbackPath = feedback.feedbackId

        var imageRef = FirebaseStorage.getInstance().reference.child("feedback/$fileName")

        imageRef.putFile(ImageUri)
            .addOnSuccessListener {
                Log.d("Save", "Successful : ${it.metadata?.path}")
                //Toast.makeText(this@ContactInformation, "Profile Picture Changed",Toast.LENGTH_SHORT).show()
                imageRef.downloadUrl.addOnSuccessListener {
                    Log.d("Save", "Successful : $it")

                    val ref = FirebaseDatabase.getInstance().getReference("/Feedback").child(feedbackPath)
                    ref.child("image").setValue(it.toString())

                }

            }.addOnFailureListener {
                Toast.makeText(this@ContactInformation, "Failed",Toast.LENGTH_SHORT).show()
            }


    }


    private fun navigationDrawer() {

        /*------------Navigation Drawer Menu--------------*/

        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.nav_contact)

        menuIcon.setOnClickListener(View.OnClickListener {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else
                drawerLayout.openDrawer(GravityCompat.START)
        })

    }



    override fun onBackPressed() {


        /*------------Navigation Drawer Menu--------------*/
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.nav_drawer_open,
            R.string.nav_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)

        }
        else {
            //super.onBackPressed()
            val intent = Intent(this@ContactInformation, Homepage::class.java)
            startActivity(intent)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this@ContactInformation, Homepage::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {
                val intent = Intent(this@ContactInformation, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_favourites -> {
                val intent = Intent(this@ContactInformation, Favourites::class.java)
                startActivity(intent)
            }
            R.id.nav_order_history -> {
                val intent = Intent(this@ContactInformation, PurchaseHistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_orders -> {
                val intent = Intent(this@ContactInformation, ActiveOrders::class.java)
                startActivity(intent)
            }
            R.id.nav_events -> {
                val calendarUri: Uri = CalendarContract.CONTENT_URI
                    .buildUpon()
                    .appendPath("time")
                    .build()
                startActivity(Intent(Intent.ACTION_VIEW, calendarUri))
                navigationView.setCheckedItem(R.id.nav_events)
            }
            R.id.nav_logout -> {
                val intent = Intent(this@ContactInformation, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                val intent = Intent(this@ContactInformation, ShareActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_contact -> {
                val intent = Intent(this@ContactInformation, ContactInformation::class.java)
                startActivity(intent)
            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



}