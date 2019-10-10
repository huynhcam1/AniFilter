package com.anifilter.filter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Lifecycle;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.otaliastudios.cameraview.CameraView;

public class MainActivity extends AppCompatActivity {

    CameraView cameraView;
    OverlayView overlayView;
    private final int PERMISSION_REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraView = findViewById(R.id.camera_view);
        checkAndRequestCameraPermission();
    }

    private void checkAndRequestCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            startFaceDetector();
        } else {
             startFaceDetector();
        }
    }

    private void startFaceDetector() {
        Lifecycle lifecycle = getLifecycle();
        lifecycle.addObserver(new MainActivityLifecycleObserver(cameraView));
        FaceDetector faceDetector =  new FaceDetector(cameraView, overlayView);
        faceDetector.startProcessing();
    }

}
