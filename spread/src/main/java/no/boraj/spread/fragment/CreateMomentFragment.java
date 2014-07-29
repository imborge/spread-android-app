package no.boraj.spread.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;

import java.io.ByteArrayOutputStream;

import no.boraj.spread.R;
import no.boraj.spread.model.ResponseStatus;
import no.boraj.spread.task.AsyncTaskListener;
import no.boraj.spread.task.UploadMomentTask;

public class CreateMomentFragment extends Fragment implements AsyncTaskListener<ResponseStatus> {
    private String TAG = "CreateMomentFragment";
    private ImageView momentImage;
    private TextView momentText;
    private byte[] photoData;

    public static CreateMomentFragment newInstance(byte[] photoData) {
        CreateMomentFragment fragment = new CreateMomentFragment();
        Bundle args = new Bundle();
        args.putByteArray("photoData", photoData);
        fragment.setArguments(args);
        return fragment;
    }

    private byte[] processImageData(byte[] data) {
        // Decode to Bitmap
        Bitmap momentBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        // Scale
        momentBitmap = Bitmap.createScaledBitmap(momentBitmap, momentBitmap.getWidth() / 2, momentBitmap.getHeight() / 2, true);

        // Rotate image
        if (momentBitmap.getWidth() > momentBitmap.getHeight()) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            momentBitmap = Bitmap.createBitmap(momentBitmap, 0, 0, momentBitmap.getWidth(), momentBitmap.getHeight(), matrix, true);
        }

        momentImage.setImageBitmap(momentBitmap);
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        momentBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

        return stream.toByteArray();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view =  inflater.inflate(R.layout.fragment_create_moment, container, false);

        momentImage = (ImageView) view.findViewById(R.id.create_moment_photo);
        momentText = (TextView) view.findViewById(R.id.create_moment_text);

        // Get image
        photoData = processImageData(getArguments().getByteArray("photoData"));

        momentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open dialog to send friend request
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setTitle("Add text");

                final EditText input = new EditText(getActivity());
                alert.setView(input);

                alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        momentText.setText(input.getText());
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dont do shit
                    }
                });

                alert.show();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.create_moment, menu);

        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_share) {
            new UploadMomentTask(photoData, momentText.getText().toString(), this).execute(Session.getActiveSession().getAccessToken());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onPostExecute(ResponseStatus result) {
        Toast t = Toast.makeText(getActivity(), result.getResponseStatus(), Toast.LENGTH_SHORT);
        t.show();
    }
}
