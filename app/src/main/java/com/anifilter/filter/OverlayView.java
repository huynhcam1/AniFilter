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


    public OverlayView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setFace(FirebaseVisionFace face) {
        this.face = face;
        postInvalidate();
    }

    public void setPreviewWidth(int width) {
        this.previewWidth = width;
    }

    public void setPreviewHeight(int height) {
        this.previewHeight = height;
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
        }
    }

    private void drawGlasses(Canvas canvas, FirebaseVisionFace face) {
        FirebaseVisionFaceLandmark leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
        FirebaseVisionFaceLandmark rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE);
        if (leftEye != null && rightEye != null) {
            // translate camera view coordinate system to canvas coordinate system
            Float leftEyeX = (previewWidth - leftEye.getPosition().getX()) * widthScaleFactor;
            Float rightEyeX = (previewWidth - rightEye.getPosition().getX()) * widthScaleFactor;
            Float leftEyeY = leftEye.getPosition().getY() * heightScaleFactor;
            Float rightEyeY = rightEye.getPosition().getY() * heightScaleFactor;
            Float eyeDistance = rightEyeX - leftEyeX;
            Float delta = eyeDistance / 2; // set boundary of glasses to be half of eye distance
            int left = leftEyeX.intValue() - delta.intValue();
            int top = leftEyeY.intValue() + delta.intValue();
            int right = rightEyeX.intValue() + delta.intValue();
            int bottom = rightEyeY.intValue() - delta.intValue();
            int offset = 200; // offset value to reposition glasses (tested on S6)
            // create dimensions and draw glasses on canvas
            Rect glassesRect = new Rect(left+offset, top, right+offset, bottom);
            canvas.drawBitmap(glasses, null, glassesRect, null);
        }
    }
}
