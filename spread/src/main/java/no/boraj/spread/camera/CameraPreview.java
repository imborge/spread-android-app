package no.boraj.spread.camera;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder holder;
	private Camera camera;
	private String TAG = "CameraPreview";
	
	public CameraPreview(Context context, Camera camera) {
		super(context);
		this.camera = camera;

		// Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        holder = getHolder();
        holder.addCallback(this);
        // Deprecated setting, but required on android versions prior to 3.0
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// The surface has been created, now tell the camera where to draw the preview
		try {
            for(Camera.Size size : camera.getParameters().getSupportedPreviewSizes()) {

                Log.d(TAG, String.format("%sx%s", size.width, size.height));
            }
            camera.setDisplayOrientation(90);
			camera.setPreviewDisplay(holder);
			camera.startPreview();
		} catch (IOException e) {
			Log.d(TAG, e.getMessage());
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Empty. Take care of releasing the camera preview in activity.
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.
		
		if (holder.getSurface() == null) {
			// Preview surface does not exist.
			return;
		}
		
		// Stop the camera before making changes.
		try {
			camera.stopPreview();
		} catch (Exception e) {
			// Do nothing, tried to stop a non-existing preview.
            return;
		}
		
		// Set preview size and make any resize, rotate or 
		// reformatting here.
		camera.setDisplayOrientation(90);

        //Camera.Parameters p = camera.getParameters();
        //p.set("orientation", "portrait");
        //p.set("rotation", 90);
        //camera.setParameters(p);
		
		// Start preview with new settings.
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();
		} catch (Exception e) {
            Log.d(TAG, "Error starting the camera preview: " + e.getMessage());
            Log.d(TAG, "Trying again...");
		}
	}
}
