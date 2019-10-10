package com.anifilter.filter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Frame;
import com.otaliastudios.cameraview.FrameProcessor;
import com.otaliastudios.cameraview.Size;

public class FaceDetector {

    private CameraView cameraView;
    private OverlayView overlayView;
    private FirebaseVisionFaceDetectorOptions options;
    private FirebaseVisionFaceDetector detector;

    public FaceDetector(CameraView cameraView, OverlayView overlayView) {
        this.cameraView = cameraView;
        this.overlayView = overlayView;
        FirebaseVisionFaceDetectorOptions.Builder fdo = new FirebaseVisionFaceDetectorOptions.Builder();
        options = fdo.setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS).build();
        detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
    }

    void startProcessing() {
        cameraView.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {
                byte[] data = frame.getData();
                int rotation = frame.getRotation();
                long time = frame.getTime();
                Size size = frame.getSize();
                int format = frame.getFormat();
            }
        });
    }
}
