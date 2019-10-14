package com.anifilter.filter;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.otaliastudios.cameraview.CameraView;

/* This is a observer class that observes the state of the camera view.
* ie. pauses the application if user clicks menu button on phone */
public class MainActivityLifecycleObserver implements LifecycleObserver {

    private CameraView cameraView;

    public MainActivityLifecycleObserver(CameraView cameraView) {
        this.cameraView = cameraView;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void startCamera() {
        cameraView.open();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pauseCamera() {
        cameraView.close();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroyCamera() {
        cameraView.destroy();
    }

}
