package com.example.ardrillplacement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import android.widget.TextView
import android.widget.Toast
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import java.io.File

class ArActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment
    private var modelRenderable: ModelRenderable? = null
    private var placedAnchorNode: AnchorNode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        arFragment = supportFragmentManager.findFragmentById(R.id.ar_fragment) as ArFragment
        val instructionsText: TextView = findViewById(R.id.instructions_text)

        val modelPath = intent.getStringExtra("MODEL_PATH") ?: "models/drill.glb"

        buildModel(modelPath)

        arFragment.setOnTapArPlaneListener { hitResult, plane, _ ->
            if (plane.type == Plane.Type.HORIZONTAL_UPWARD_FACING) {
                placedAnchorNode?.let {
                    arFragment.arSceneView.scene.removeChild(it)
                    it.anchor?.detach()
                    it.setParent(null)
                }

                val anchor = hitResult.createAnchor()
                placeObject(anchor)
            }
        }

        arFragment.arSceneView.scene.addOnUpdateListener {
            val frame = arFragment.arSceneView.arFrame
            if (frame != null) {
                val planes = frame.getUpdatedTrackables(Plane::class.java)
                if (planes.any { it.trackingState == com.google.ar.core.TrackingState.TRACKING }) {
                    instructionsText.text = "Tap on the plane to place the drill!"
                } else {
                    instructionsText.text = "Scan the floor to detect a plane..."
                }
            }
        }
    }


    private fun buildModel(modelPath: String) {
        ModelRenderable.builder()
            .setSource(this, RenderableSource.builder().setSource(
                this,
                Uri.parse(modelPath),
                RenderableSource.SourceType.GLB)
                .setScale(0.25f)
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build())
            .setRegistryId(modelPath)
            .build()
            .thenAccept { renderable ->
                modelRenderable = renderable
                Toast.makeText(this, "Model loaded successfully", Toast.LENGTH_SHORT).show()
            }
            .exceptionally { throwable ->
                Toast.makeText(this, "Failed to load model: ${throwable.message}", Toast.LENGTH_LONG).show()
                null
            }
    }


    private fun placeObject(anchor: Anchor) {
        if (modelRenderable == null) {
            Toast.makeText(this, "Model is not loaded yet", Toast.LENGTH_SHORT).show()
            return
        }

        val anchorNode = AnchorNode(anchor)
        anchorNode.setParent(arFragment.arSceneView.scene)

        val modelNode = TransformableNode(arFragment.transformationSystem)
        modelNode.setParent(anchorNode)
        modelNode.renderable = modelRenderable
        modelNode.select()

        placedAnchorNode = anchorNode
    }
}
