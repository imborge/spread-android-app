package no.boraj.spread.fragment;

import no.boraj.spread.R;
import no.boraj.spread.Util;
import no.boraj.spread.camera.CameraPreview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static no.boraj.spread.Util.getOutputMediaFile;

public class TakePhotoFragment extends Fragment {
	private String TAG = "TakePhotoFragment";
	private Camera camera;
	private CameraPreview cameraPreview;
	private FrameLayout preview;

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // Send photo data to CreateMomentFragment
            CreateMomentFragment createMomentFragment = CreateMomentFragment.newInstance(data);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, createMomentFragment)
                    .commit();
        }
    };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}


	
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
            
            Camera.Parameters params = c.getParameters();
            //params.setPictureSize();
		} catch (Exception e) {
			// Could not open camera, return null
		}

		
		return c;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_take_photo, container, false);
		
		// Create our preview view and set it as the content of our fragment
		preview = (FrameLayout) view.findViewById(R.id.camera_preview);

        // Capture image
        ImageButton buttonCapture = (ImageButton) view.findViewById(R.id.button_capture);
        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback);
            }
        });
		
		return view;
	}



    public void cleanUp() {
        if (camera != null) {
            camera.release();

            preview.removeView(cameraPreview);
            camera = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        cleanUp();
    }
	
	@Override
	public void onResume() {
		super.onResume();
        camera = getCameraInstance();
        cameraPreview = new CameraPreview(getActivity(), camera);
        preview.addView(cameraPreview);
	}
}
