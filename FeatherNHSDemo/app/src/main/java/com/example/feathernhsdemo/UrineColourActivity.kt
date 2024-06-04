package com.example.feathernhsdemo

import android.content.Intent
import android.os.Bundle
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
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import kotlin.system.exitProcess


//Urine scale is from NHS Inform - https://www.nhsinform.scot/campaigns/hydration/
class UrineColourActivity: AppCompatActivity(), OnRobotReadyListener, Robot.TtsListener  {

    lateinit var sRobot: Robot
    // Create unique IDs for each TtsRequest
    val ttsRequestBye = "TTS_BYE"

    // Map TtsRequests to their IDs
    val ttsRequestMap = mapOf(
        ttsRequestBye to TtsRequest.create("Thank you. It has been great to interact with you today Bill. Have a great rest of your day.", false),
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.urine_colour_activity)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.urine_colour_activity_id)) { v, insets ->
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
        val ttsRequest = TtsRequest.create("Please use the sliding bar to indicate the colour of your urine on my tablet.", false)
        sRobot.speak(ttsRequest)
        interactionLogic()
    }

    fun interactionLogic(){
        val button = findViewById<Button>(R.id.submitbutton_urinecolour)
        button.setOnClickListener(View.OnClickListener {

            // Get the ID of the selected radio button
            val seekBarValue = findViewById<SeekBar>(R.id.urineSeekBar).progress
            Log.i("Seek bar value", seekBarValue.toString())
            ttsRequestMap[ttsRequestBye]?.let { it1 -> sRobot.speak(it1) }

            sRobot.addTtsListener(object : Robot.TtsListener {
                override fun onTtsStatusChanged(ttsRequest: TtsRequest) {
                    // Iterate over the map entries to find the matching ID
                    ttsRequestMap.entries.forEach { (id, request) ->
                        if (request == ttsRequest && ttsRequest.status == TtsRequest.Status.COMPLETED) {
                            // Action based on the ID
                            when (id) {
                                ttsRequestBye -> {
                                    Log.i("MOVE", "MOVE")
                                    sRobot.goTo("home base")
                                    //Close app when finished
                                    finishAffinity()
                                    exitProcess(0)
                                }

                            }
                        }
                    }
                }
            })

        })

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