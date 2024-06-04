package com.example.feathernhsdemo


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.listeners.OnRobotReadyListener

class HydrationActivity: AppCompatActivity(), OnRobotReadyListener, Robot.TtsListener {

    lateinit var sRobot: Robot

    var delayMillis: Long = 4000 // Adjust this value as needed
    val handler = Handler(Looper.getMainLooper())

    // Create unique IDs for each TtsRequest
    val ttsRequestHydrated = "TTS_HYDRATED"
    val ttsRequestDehydrated = "TTS_DEHYDRATED"

    // Map TtsRequests to their IDs
    val ttsRequestMap = mapOf(
        ttsRequestHydrated to TtsRequest.create("I am glad to hear you are keeping hydrated. It is really important for your overall health, and it can help prevent urinary tract infections. Since you're keeping so hydrated, you must have had to use the toilet multiple times today.", false),
        ttsRequestDehydrated to TtsRequest.create("It is really important to keep hydrated not only for your overall health, but also for preventing urinary tract infections. Dehydration can effect the colour of your urine.", false)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.hydration_activity)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.hydration_activity_id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        sRobot = Robot.getInstance()
    }

    override fun onStart() {
        super.onStart()
        // Add robot event listeners
        sRobot.addOnRobotReadyListener(this)
        startInteraction()
    }

    fun startInteraction(){
        val ttsRequest = TtsRequest.create("I have another question for you. How many glasses of water did you drink today?", false)
        sRobot.speak(ttsRequest)
        interactionLogic()

//        handler.postDelayed({
//            delayMillis = 7000
//            val ttsRequest = TtsRequest.create("Great, thanks. ", false)
//            sRobot.speak(ttsRequest)
//            handler.postDelayed({
//                interactionLogic()
//            }, delayMillis)
//        }, delayMillis)
    }

    fun interactionLogic(){
        val button = findViewById<Button>(R.id.watersubmitbutton)
        button.setOnClickListener(View.OnClickListener {

            // Get the ID of the selected radio button
            val seekBarValue = findViewById<SeekBar>(R.id.seekBar).progress
            Log.i("Seek bar value", seekBarValue.toString())

            //Give different responses dependent on number of glasses of water indicated
            if (seekBarValue < 4) {
                ttsRequestMap[ttsRequestDehydrated]?.let { it1 -> sRobot.speak(it1) }
            }

            else if (seekBarValue >=4) {
                ttsRequestMap[ttsRequestHydrated]?.let { it1 -> sRobot.speak(it1) }
            }


            // Set up TtsListener to handle status changes
            sRobot.addTtsListener(object : Robot.TtsListener {
                override fun onTtsStatusChanged(ttsRequest: TtsRequest) {
                    // Iterate over the map entries to find the matching ID
                    ttsRequestMap.entries.forEach { (id, request) ->
                        if (request == ttsRequest && ttsRequest.status == TtsRequest.Status.COMPLETED) {
                            // Action based on the ID
                            when (id) {
                                ttsRequestDehydrated -> {
                                    Log.i("Start activity - dehydrated", "start activity")
                                    val intent = Intent(this@HydrationActivity, UrineColourActivity::class.java)
                                    startActivity(intent)
                                }

                                ttsRequestHydrated -> {
                                    Log.i("Start activity - hydrated", "start activity")
                                    val intent = Intent(this@HydrationActivity, UrineColourActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }
            })
        })
    }


    override fun onStop() {
        super.onStop()
        // Remove robot event listeners
        sRobot.removeOnRobotReadyListener(this);
    }


    override fun onTtsStatusChanged(ttsRequest: TtsRequest) {
        Log.i("Status of TTS Request", "Status:" + ttsRequest.status)
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {
            Log.i("Robot ready", "Robot is ready")
            Log.i("User tracking", "Set track user: ON");
            sRobot.trackUserOn = true // Set tracking mode on
        }
    }


}