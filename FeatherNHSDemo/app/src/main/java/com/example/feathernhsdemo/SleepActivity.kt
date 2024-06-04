package com.example.feathernhsdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.listeners.OnRobotReadyListener
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener


class SleepActivity : AppCompatActivity(), OnRobotReadyListener, Robot.TtsListener {

    lateinit var sRobot: Robot
    // Create unique IDs for each TtsRequest
    val ttsRequestBadSleep = "TTS_BAD_SLEEP"
    val ttsRequestGoodSleep = "TTS_GOOD_SLEEP"

    // Map TtsRequests to their IDs
    val ttsRequestMap = mapOf(
        ttsRequestBadSleep to TtsRequest.create("I'm sorry to hear you didn't get much sleep last night. I hope you are able to get more sleep tonight.", false),
        ttsRequestGoodSleep to TtsRequest.create("I'm glad to hear you had a good amount of sleep.", false)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.sleep_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        sRobot = Robot.getInstance()


        // Create an array of permission strings
        val permissions = arrayOf(
            "com.robotemi.permission.sequence",
            "com.robotemi.permission.settings",
            "com.robotemi.permission.face_recognition",
            "com.robotemi.permission.map"
        )

        // Request permissions with the list
        requestPermissions(permissions, 123)
//        val permissionCheck = checkSelfPermission("com.robotemi.permission.sequence")
//        val permissionCheck2 = checkSelfPermission("com.robotemi.permission.settings")
//        val permissionCheck3 = checkSelfPermission("com.robotemi.permission.face_recognition")
//        val permissionCheck4 = checkSelfPermission("com.robotemi.permission.map")

//        Log.i("PERMISSION CHECK: ", permissionCheck.toString())
//        Log.i("PERMISSION CHECK: ", permissionCheck2.toString())
//        Log.i("PERMISSION CHECK: ", permissionCheck3.toString())
//        Log.i("PERMISSION CHECK: ", permissionCheck4.toString())



    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 123) { // Use the same request code you used in requestPermissions()
            for (i in permissions.indices) {
                Log.i("PERMISSION CHECK: ", "${permissions[i]}: ${grantResults[i]}")
            }
        }
    }


    override fun onStart() {
        super.onStart()

        // Add robot event listeners
        sRobot.addOnRobotReadyListener(this)
        sRobot.addTtsListener(this)
        Log.i("Listeners added", "listeners added")

//        // Get the list of sequences
//        val sequences = sRobot.getAllSequences()
//
//        // Check if sequences are available
//        if (sequences != null && sequences.isNotEmpty()) {
//            // Iterate over the list of sequences and print their details
//            for (sequence in sequences) {
//                Log.i("Sequence Info", "Sequence ID: ${sequence.id}")
//                Log.i("Sequence Info", "Sequence Name: ${sequence.name}")
//                // Log other details as needed
//                Log.i("Sequence Info", "-----------------------------------")
//            }
//        } else {
//            Log.i("Sequence Info", "No sequences available.")
//        }
    }

    override fun onStop() {
        super.onStop()
        sRobot.removeOnRobotReadyListener(this)
    }

    fun interactionLogic(){
        val button = findViewById<Button>(R.id.sleepsubmitbutton)
        button.setOnClickListener(View.OnClickListener {
            val radioGroup = findViewById<RadioGroup>(R.id.sleepmultiplechoice)

            // Get the ID of the selected radio button
            val selectedRadioButtonId: Int = radioGroup.checkedRadioButtonId

            // Check if any radio button is selected
            if (selectedRadioButtonId != -1) {
                // Find the selected radio button by ID
                val selectedRadioButton: RadioButton = findViewById(selectedRadioButtonId)

                // Get the text of the selected radio button
                val selectedText: String = selectedRadioButton.text.toString()

                // Now you have the selected radio button's text (selectedText)
//                Log.i("SELECTED SLEEP: ","Selected option: $selectedText")

                //Have Temi respond sympathetically if they did not get much sleep (0-3 and 4-6 hours)
                if(selectedRadioButton=== findViewById<RadioButton>(R.id.zeroto3)||selectedRadioButton=== findViewById<RadioButton>(R.id.fourto6)){
                    //sRobot.playSequence("Temi Face")
                    //Log.i("TEMI FACE", "TEMI FACE")
                    ttsRequestMap[ttsRequestBadSleep]?.let { it1 -> sRobot.speak(it1/* provide default TtsRequest here*/) }

                }
                //If they did get a good amount of sleep, give positive reinforcement (7-9 or 10+ hours)
                else if(selectedRadioButton=== findViewById<RadioButton>(R.id.sevento9)||selectedRadioButton=== findViewById<RadioButton>(R.id.tenplus)){
                    ttsRequestMap[ttsRequestGoodSleep]?.let { it1 -> sRobot.speak(it1) }

                }

                // Set up TtsListener to handle status changes
                sRobot.addTtsListener(object : Robot.TtsListener {
                    override fun onTtsStatusChanged(ttsRequest: TtsRequest) {
                        // Iterate over the map entries to find the matching ID
                        ttsRequestMap.entries.forEach { (id, request) ->
                            if (request == ttsRequest && ttsRequest.status == TtsRequest.Status.COMPLETED) {
                                // Action based on the ID
                                when (id) {
                                    ttsRequestBadSleep -> {
                                        Log.i("Start activity", "start activity")
                                        val intent = Intent(this@SleepActivity, HydrationActivity::class.java)
                                        startActivity(intent)
                                        Log.i("Started activity", "started activity")
                                    }

                                    ttsRequestGoodSleep -> {
                                        Log.i("Start activity", "start activity")
                                        val intent = Intent(this@SleepActivity, HydrationActivity::class.java)
                                        startActivity(intent)
                                        Log.i("Started activity", "started activity")
                                    }
                                }
                            }
                        }
                    }
                })


            } else {
                // No radio button is selected
                Log.i("No option selected", "No option selected")
                val ttsRequest = TtsRequest.create("Please select an answer before pressing submit", false)
                sRobot.speak(ttsRequest)
            }
        })
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {
            Log.i("Robot ready", "Robot is ready")
            Log.i("User tracking", "Set track user: ON")
            sRobot.trackUserOn = true // Set tracking mode on
            Log.i("LOCATIONS", sRobot.locations.toString())
            sRobot.goTo("kitchen sink")
            // Set up listener to receive updates about the robot's movement
            sRobot.addOnGoToLocationStatusChangedListener(object : OnGoToLocationStatusChangedListener {
                override fun onGoToLocationStatusChanged(
                    location: String,
                    status: String,
                    descriptionId: Int,
                    description: String
                ) {
                    if (status == "complete" && location == "kitchen sink") {
                        Log.i("ARRIVED", "ARRIVED")
                        //Speech
                        val ttsRequest = TtsRequest.create("Hi Bill, I'm Temi. It's lovely to meet you. Could you indicate on my tablet how much sleep you had last night?", false)
                        sRobot.speak(ttsRequest)
                        interactionLogic()
                    }
                }
            })

        }
    }

    override fun onTtsStatusChanged(ttsRequest: TtsRequest) {
        Log.i("Status of TTS Request", "Status:" + ttsRequest.status)
    }


}