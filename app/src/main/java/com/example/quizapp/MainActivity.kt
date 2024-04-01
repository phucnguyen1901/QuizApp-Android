package com.example.quizapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var quizModelList: MutableList<QuizModel>
    lateinit var adapter: QuizListAdapter

    private lateinit var database: DatabaseReference
// ...

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG)

        quizModelList = mutableListOf()
        getUnsafeOkHttpClient()
        database = Firebase.database.reference
        Log.e("database<3", "$database")
//        getDataFromFirebase()
    }

    fun setupRecyclerView() {
        adapter = QuizListAdapter(quizModelList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    fun getDataFromFirebase() {
        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World! I'm a android developer")
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue()
                Log.d("tabh", "Value is: ${dataSnapshot.children}")
                for (snapshot in dataSnapshot.children) {
                    Log.e("tabgh", "$snapshot")
                    val quizModel = snapshot.getValue(QuizModel::class.java)
                    if (quizModel != null) {
                        quizModelList.add(quizModel)
                    }
                }
//
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("tabh", "Failed to read value.", error.toException())
            }
        })
// Write a message to the database
//        FirebaseDatabase.getInstance().reference.get().addOnSuccessListener { dataSnapshot ->
//            if (dataSnapshot.exists()) {
//                for (snapshot in dataSnapshot.children) {
//                    val quizModel = snapshot.getValue(QuizModel::class.java)
//                    if (quizModel != null) {
//                        quizModelList.add(quizModel)
//                    }
//                }
//            }
////            setupRecyclerView()
//        }.addOnFailureListener { error ->
//            Log.e("Firebase rror", "Error fetching data: ${error.message}", error)
//        }
//

    }
    fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { hostname, session -> true }
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }



}