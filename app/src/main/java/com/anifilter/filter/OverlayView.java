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
    int previewWidth;
    int previewHeight;
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

}
