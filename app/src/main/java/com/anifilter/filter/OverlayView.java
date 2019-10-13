package com.anifilter.filter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.*;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

public class OverlayView extends View {

    FirebaseVisionFace face;
    int previewWidth = -1;
    int previewHeight = -1;
    private float widthScaleFactor = 1.0f;
    private float heightScaleFactor = 1.0f;
    private Bitmap glasses = BitmapFactory.decodeResource(getResources(), R.drawable.glasses);
    private Bitmap cigarette = BitmapFactory.decodeResource(getResources(), R.drawable.cigarette);


    public OverlayView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setFace(FirebaseVisionFace face) {
        this.face = face;
        postInvalidate();
    }

    public void setPreviewWidth(int width) {
        this.previewWidth = width;
//        postInvalidate();
    }

    public void setPreviewHeight(int height) {
        this.previewHeight = height;
//        postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (face != null && canvas != null) {
            if (previewWidth != -1 && previewHeight != -1) {
                widthScaleFactor = (float) getWidth() / previewWidth;
                heightScaleFactor = (float) getHeight() / previewHeight;
            }
            drawGlasses(canvas, face);
            drawCigarette(canvas, face);
        }
    }

    private void drawGlasses(Canvas canvas, FirebaseVisionFace face) {
        FirebaseVisionFaceLandmark leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
        FirebaseVisionFaceLandmark rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE);
        if (leftEye != null && rightEye != null) {
            Float eyeDistance = leftEye.getPosition().getX() - rightEye.getPosition().getX();
            Float delta = (widthScaleFactor * eyeDistance / 2);
            int left = translateX(leftEye.getPosition().getX()).intValue() - delta.intValue();
            int top = translateY(leftEye.getPosition().getY()).intValue() + delta.intValue();
            int right = translateX(rightEye.getPosition().getX()).intValue() + delta.intValue();
            int bottom = translateY(rightEye.getPosition().getY()).intValue() - delta.intValue();
            Rect glassesRect = new Rect(left, top, right, bottom);
            canvas.drawBitmap(glasses, null, glassesRect, null);
        }
    }

    private void drawCigarette(Canvas canvas, FirebaseVisionFace face) {
        FirebaseVisionFaceLandmark leftMouth = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_LEFT);
        FirebaseVisionFaceLandmark rightMouth = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_RIGHT);
        if (leftMouth != null && rightMouth != null) {
            Float mouthLength = (leftMouth.getPosition().getX() - rightMouth.getPosition().getX()) * widthScaleFactor;
            int left = translateX(leftMouth.getPosition().getX()).intValue() - mouthLength.intValue();
            int top = translateY(leftMouth.getPosition().getY()).intValue() + mouthLength.intValue();
            int right = translateX(rightMouth.getPosition().getX()).intValue();
            int bottom = translateY(rightMouth.getPosition().getY()).intValue();
            Rect glassesRect = new Rect(left, top, right, bottom);
            canvas.drawBitmap(cigarette, null, glassesRect, null);
        }
    }

    private Float translateX(Float f) {
        return getWidth() - scaleX(f);
    }

    private Float translateY(Float f) {
        return scaleY(f);
    }

    private float scaleX(Float f) {
        return f * widthScaleFactor;
    }

    private float scaleY(Float f) {
        return f * heightScaleFactor;
    }
}
