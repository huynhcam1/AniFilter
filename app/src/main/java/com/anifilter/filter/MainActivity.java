package com.anifilter.filter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Lifecycle;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.otaliastudios.cameraview.CameraView;

import static com.otaliastudios.cameraview.CameraView.PERMISSION_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    CameraView cameraView;
    OverlayView overlayView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraView = findViewById(R.id.camera_view);
        overlayView = findViewById(R.id.overlay_view);
        button = findViewById(R.id.button_id);
        checkAndRequestCameraPermission();
    }

    private void checkAndRequestCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }
        startFaceDetector();
    }

    private void startFaceDetector() {
        // initialize a lifecycle to observe the state of the camera view
        Lifecycle lifecycle = getLifecycle();
        lifecycle.addObserver(new MainActivityLifecycleObserver(cameraView));
        // start face detection
        final FaceDetector faceDetector =  new FaceDetector(cameraView, overlayView);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                faceDetector.startProcessing();
            }
        });
    }

}
