package com.example.ardrillplacement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var drillAdapter: DrillAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //dummy data
        val drillList = listOf(
            Drill(
                "Impact Drill",
                "High-torque for driving screws and bolts.",
                R.drawable.drill_impact,
                "models/drill.glb"
            ),
            Drill(
                "Hammer Drill",
                "Combines rotation with hammer action.",
                R.drawable.drill_hammer,
                "models/drillhammer.glb"
            ),
            Drill(
                "Cordless Drill",
                "Portable and versatile for general tasks.",
                R.drawable.drill_cordless,
                "models/drillcordless.glb"
            )
        )

        val drillsRecyclerView: RecyclerView = findViewById(R.id.drills_recycler_view)
        drillsRecyclerView.layoutManager = LinearLayoutManager(this)

        drillAdapter = DrillAdapter(drillList) {

        }
        drillsRecyclerView.adapter = drillAdapter

        val startArButton: Button = findViewById(R.id.start_ar_button)
        startArButton.setOnClickListener {
            val selectedDrill = drillAdapter.getSelectedDrill()

            val intent = Intent(this, ArActivity::class.java).apply {
                putExtra("MODEL_PATH", selectedDrill.modelPath)
            }
            startActivity(intent)
        }
    }
}
