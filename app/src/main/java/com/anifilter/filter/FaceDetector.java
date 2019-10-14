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
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.otaliastudios.cameraview.size.Size;

import java.util.List;

public class FaceDetector {

    private CameraView cameraView;
    private OverlayView overlayView;
    private FirebaseVisionFaceDetectorOptions options;
    private FirebaseVisionFaceDetector detector;

    public FaceDetector(CameraView cameraView, OverlayView overlayView) {
        this.cameraView = cameraView;
        this.overlayView = overlayView;
        // select options to allow face landmarks, then initialize face detector
        FirebaseVisionFaceDetectorOptions.Builder fdo = new FirebaseVisionFaceDetectorOptions.Builder();
        options = fdo.setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS).build();
        detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
    }

    void startProcessing() {
        // obtain the frames rom the camera view
        cameraView.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {
                byte[] data = frame.getData();
                // rotation represented in int value instead of degrees for meta data
                final int rotation = frame.getRotation() / 90;
                Size size = frame.getSize();
                // define image parameters ie. width, height, color encoding format, rotation
                FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                        .setWidth(size.getWidth())
                        .setHeight(size.getHeight())
                        .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                        .setRotation(rotation)
                        .build();
                // buil image using the frame data and metadata
                FirebaseVisionImage image = FirebaseVisionImage.fromByteArray(data, metadata);
                // if detector detects a face, transfer information to overlay view to generate filter
                Task<List<FirebaseVisionFace>> task = detector.detectInImage(image);
                task.addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                        if (firebaseVisionFaces.size() > 0) {
                            if (cameraView.getSnapshotSize() != null) {
                                int width = cameraView.getSnapshotSize().getWidth();
                                int height = cameraView.getSnapshotSize().getHeight();
                                if (rotation / 2 == 1) { // rotation = 3 if phone is upright
                                    overlayView.setPreviewWidth(width);
                                    overlayView.setPreviewHeight(height);
                                } else {
                                    overlayView.setPreviewWidth(height);
                                    overlayView.setPreviewHeight(width);
                                }
                            }
                            overlayView.setFace(firebaseVisionFaces.get(0));
                        }
                    }
                });
            }
        });
    }
}
